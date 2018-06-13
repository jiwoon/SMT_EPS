$(function () {
    $.fn.handleTable = function (options) {
        //1.Settings 初始化设置
        var c = $.extend({
            "operatePos": -1, //-1表示默认操作列为最后一列
            "handleFirst": false, //第一行是否作为操作的对象
            "edit": "编辑",
            "save": "保存",
            "cancel": "取消",
            "add": "添加",
            "confirm": "确认",
            "del": "删除",
            "editableCols": "all", //可编辑的列，从0开始
            //"pos" : 0, //位置位于该列开头，还是结尾（左侧或右侧）
            "order": ["edit", "add", "del"], //指定三个功能的顺序
            "saveCallback": function (data, isSuccess, etcData) { //这里可以写ajax内容，用于保存编辑后的内容
                //data: 返回的数据
                //isSuccess: 方法，用于保存数据成功后，将可编辑状态变为不可编辑状态
                //ajax请求成功（保存数据成功），才回调isSuccess函数（修改保存状态为编辑状态）
            },
            "addCallback": function (data, isSuccess, etcData) {
                //isSuccess: 方法，用于添加数据成功后，将可编辑状态变为不可编辑状态
            },
            "delCallback": function (isSuccess, etcData) {
                //isSuccess: 方法，用于删除数据成功后，将对应行删除
            }
        }, options);

        var editStatus = false; //单行修改状态

        //表格的列数
        var colsNum = $(this).find('tr').last().children().size();

        //2.初始化操作列，默认为最后一列，从1算起
        if (c.operatePos == -1) {
            c.operatePos = colsNum - 1;
        }

        //3.获取所有需要被操作的行
        var rows = $(this).find('tr');
        if (!c.handleFirst) {
            rows = rows.not(":eq(0)");
        }

        //4.获取放置“操作”的列，通过operatePos获取
        var rowsTd = [];
        var allTd = rows.children();
        for (var i = c.operatePos; i <= allTd.size(); i += colsNum) {
            if (c.handleFirst) { //如果操作第一行，就把放置操作的列内容置为空
                allTd.eq(i).html("");
            }
            rowsTd.push(allTd.eq(i)[0]);
        }

        //6.修改设置 order 为空时的默认值
        if (c.order.length == 0) {
            c.order = ["edit"];
        }

        //7.保存可编辑的列
        var cols = getEditableCols();

        //8.初始化链接的构建
        var saveLink = "", cancelLink = "", editLink = "", addLink = "", confirmLink = "", delLink = "";
        initLink();

        //9.初始化操作
        initFunc(c.order, rowsTd, true);

        /**
         * 创建操作链接
         */
        function createLink(str) {
            return "<a href=\"javascript:void(0)\" style=\"margin:0 3px\" title=\"" + str[1] + "\">" + str[0] + "</a>";
        }

        /**
         * 初始各种操作的链接
         */
        function initLink() {
            for (var i = 0; i < c.order.length; i++) {
                switch (c.order[i]) {
                    case "edit":
                        //“编辑”链接
                        editLink = createLink(c.edit);
                        saveLink = createLink(c.save);
                        cancelLink = createLink(c.cancel);
                        break;
                    case "add":
                        //“添加”链接
                        addLink = createLink(c.add);
                        //“确认”链接
                        confirmLink = createLink(c.confirm);
                        //“取消”链接
                        cancelLink = createLink(c.cancel);
                        break;
                    case "del":
                        //“删除”链接
                        delLink = createLink(c.del);
                        break;
                }
            }
        }

        /**
         * 获取可进行编辑操作的列
         */
        function getEditableCols() {
            var cols = c.editableCols;
            if ($.type(c.editableCols) != "array" && cols == "all") { //如果是所有列都可以编辑的话
                cols = [];
                for (var i = 0; i < colsNum; i++) {
                    if (i != c.operatePos) { //排除放置操作的列
                        cols.push(i);
                    }
                }
            } else if ($.type(c.editableCols) == "array") { //有指定选择编辑的列的话需要排序放置“编辑”功能的列
                var copyCols = [];
                for (var i = 0; i < cols.length; i++) {
                    if (cols[i] != c.operatePos) {
                        copyCols.push(cols[i]);
                    }
                }
                cols = copyCols;
            }
            return cols;
        }

        /**
         * 根据c.order参数设置提供的操作
         * @param func 需要设置的操作
         * @param cols 放置操作的列
         */
        function initFunc(func, cols, firstTimeInit) {
            for (var i = 0; i < func.length; i++) {
                var o = func[i];
                switch (o) {
                    case "edit":
                        createEdit(cols);
                        break;
                    case "add":
                        createAdd(cols);
                        break;
                    case "del":
                        createDel(cols);
                        break;
                }
            }

            if (firstTimeInit) {
                $("table tr th a")[1].remove();
                $("table tr th a")[1].remove();
                $("table tr:last-child a").remove()
            }
        }

        /**
         * 创建“编辑一行”的功能
         * @param operateCol 放置编辑操作的列
         */
        function createEdit(operateCol) {
            var beforeP;
            $(editLink).appendTo(operateCol).on("click", function () {
                    if (replaceQuote($(this).html()) == replaceQuote(c.edit[0])) { //如果此时是编辑状态
                        if (!editStatus) {
                            beforeP = $(this).parents('tr').clone();
                            editStatus = true;
                            toSave(this); //将编辑状态变为保存状态
                        } else {
                            alert("每次只能编辑一行")
                        }
                    } else if (replaceQuote($(this).html()) == replaceQuote(c.save[0])) { //如果此时是保存状态
                        var p = $(this).parents('tr'); //获取被点击的当前行
                        var data = []; //保存修改后的数据到数据内
                        for (var i = 0; i < cols.length; i++) {
                            var tr = p.children().eq(cols[i]);
                            var inputValue = tr.children('input').val();
                            data.push(inputValue);
                        }
                        data.push(p.children().eq(6).children().is(":checked"));
                        var etcData = {
                            beforeCol: beforeP
                        };

                        $this = this; //此时的this表示的是 被点击的 编辑链接
                        c.saveCallback(data, function () {
                            toEdit($this, true);
                            editStatus = false;
                        }, etcData);
                    }
            });
            var afterSave = []; //保存修改前的信息，用于用户点击取消后的数值返回操作
            //修改为“保存”状态
            function toSave(ele) {
                $(ele).html(c.save[0]); //修改为“保存”
                $(ele).attr("title", "保存");
                $(ele).after(cancelLink); //添加相应的取消保存的“取消链接”
                $(ele).next().on('click', function () {
                    //if($(this).html() == c.cancel.replace(eval("/\'/gi"),"\"")) {
                    editStatus = false;
                    toEdit(ele, false);
                    //}
                });

                //获取被点击编辑的当前行 tr jQuery对象
                var p = $(ele).parents('tr');


                afterSave = []; //清空原来保存的数据
                for (var i = 0; i < cols.length; i++) {
                    var tr = p.children().eq(cols[i]);
                    var editTr = "<input type=\"text\" class=\"form-control\" value=\"" + tr.html() + "\"/>";
                    afterSave.push(tr.html()); //保存未修改前的数据
                    tr.html(editTr);
                }
                afterSave.push(p.children().eq(6).children().is(":checked"));
                p.children().eq(6).children().attr("disabled", false);
            }

            //修改为“编辑”状态（此时，需要通过isSave标志判断是
            // 因为点击了“保存”（保存成功）变为“编辑”状态的，还是因为点击了“取消”变为“编辑”状态的）
            function toEdit(ele, isSave) {
                $(ele).html(c.edit[0]);
                $(ele).attr("title", "编辑");
                if (replaceQuote($(ele).next().html()) == replaceQuote(c.cancel[0])) {
                    $(ele).next().remove();
                }

                var p = $(ele).parents('tr');
                for (var i = 0; i < cols.length; i++) {
                    var tr = p.children().eq(cols[i]);
                    var value;
                    if (isSave) {
                        value = tr.children('input').val();
                    } else {
                        value = afterSave[i];
                    }

                    tr.html(value);
                }
                if (!isSave) {
                    if (afterSave[6]) {
                        p.children().eq(6).children().attr("checked", "checked");
                    } else {
                        p.children().eq(6).children().attr("checked", false);
                    }
                }
                p.children().eq(6).children().attr("disabled", true);
            }
        }

        /**
         * 创建“添加一行”的功能
         * @param operateCol
         */
        function createAdd(operateCol) {
            $(addLink).appendTo(operateCol).on("click", function () {
                if (!editStatus) { //获取被点击“添加”的当前行 tr jQuery对象
                    editStatus = true;
                    var p = $(this).parents('tr');
                    var etcData = {
                        nextCol: p.next()
                    };
                    var copyRow = p.clone();
                    copyRow.empty();
                    for (var x = 0; x < p.children().length; x++) {
                        copyRow.append("<td></td>");
                    }
                    //var copyRow = p.clone(); //构建新的一行
                    var input = "<input type=\"text\"/>";
                    var childLen = p.children().length;
                    for (var i = 0; i < childLen; i++) {
                        copyRow.children().eq(i).html("<input type=\"text\" class=\"form-control\"/>");
                    }

                    //最后一行是操作行
                    var last = copyRow.children().eq(c.operatePos);
                    var check = copyRow.children().eq(c.operatePos - 1);
                    last.html("");
                    check.html("<input type=\"checkbox\"/>");
                    p.after(copyRow);
                    var confirm = $(confirmLink).appendTo(last).on("click", function () {
                        var data = [];
                        for (var i = 0; i < childLen - 2; i++) {
                            if (i != c.operatePos) {
                                var v = copyRow.children().eq(i).children("input").val();
                                data.push(v);
                            }
                        }
                        data.push(copyRow.children().eq(6).children().is(":checked"));
                        c.addCallback(data, function () {
                            last.html("");
                            //------------这里可以进行修改
                            for (var i = 0; i < 6; i++) {
                                var v = copyRow.children().eq(i).children("input").val();
                                copyRow.children().eq(i).html(v);
                            }
                            copyRow.children().eq(6).children().attr("disabled", true);
                            initFunc(c.order, last);
                            editStatus = false
                        }, etcData);
                    });

                    $(confirm).after(cancelLink); //添加相应的取消保存的“取消链接”
                    $(confirm).next().on('click', function () {
                        copyRow.remove();
                        editStatus = false
                    });
                } else {
                    alert("每次只能编辑一行");
                }
            });
        }

        /**
         * 创建“删除一行”的功能
         * @param operateCol
         */
        function createDel(operateCol) {
            $(delLink).appendTo(operateCol).on("click", function () {
                var etcData = {
                    thisCol: $(this).parents('tr').clone()
                };
                var _this = this;
                c.delCallback(function () {
                    $(_this).parents('tr').remove();
                }, etcData);
            });
        }

        /**
         * 将str中的单引号转为双引号
         * @param str
         */
        function replaceQuote(str) {
            return str.replace(/\'/g, "\"");
        }
    };

});