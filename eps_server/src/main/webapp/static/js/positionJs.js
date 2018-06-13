$(function () {
    var originNum = 100;  //一开始加载的个数
    var newNum = 0;
    //鼠标放置样式
    $("#pcBtn,#fileBtn").hover(function () {
        $(this).addClass("ui-state-hover")
    }, function () {
        $(this).removeClass("ui-state-hover");
    });

    var array1 = [];  //放置站位表名
    var array2 = []; //放置工单号
    var editStack = []; //表格编辑操作栈
//    查询按钮事件
    $("#pcBtn").on("click", function () {
        $("#showWaiting").css("display", "block");
        $(window).unbind();
        var standPosition = $("#stand-position").val() == "" ? null : $("#stand-position").val();     //站位的输入内容   对应fileName
        var workNum = $("#work-num").val() == "" ? null : $("#work-num").val();     //工单的输入内容  workOrder
        var state = $("#state option:selected").val();   //状态  state
        var line = $("#line-num option:selected").text() == "不限" ? null : $("#line-num option:selected").text();  //线号 line
        $.ajax({
            url: "program/list",
            type: "post",
            dataType: "json",
            data: {
                fileName: standPosition,
                workOrder: workNum,
                state: state,
                line: line,
                ordBy: "state, create_time desc"
            },
            success: function (data) {
                $("#showWaiting").css("display", "none");
                if (data.result) {
                    alert("您没有权限！");
                    window.location.href = "/eps_server/user/goLogin";

                } else {
                    autoCreateTable(data);
                    bindEvent(data);
                    //超过100个数据时边滚动边加载

                    originNum = 100;
                    newNum = 0;
                    if (data.length > 100) {
                        $(window).on("scroll", debounce(function () {
                            var scrollTop = $(this).scrollTop();
                            var scrollHeight = $(document).height();
                            var windowHeight = $(this).height();
                            if (scrollTop + windowHeight >= scrollHeight - 20) {
                                newNum = originNum;
                                if (newNum < data.length) {
                                    originNum += 100;
                                    originNum = (originNum >= data.length ? data.length : originNum);  //判断加100后是否长度大于数据长度
                                    for (var de = newNum; de < originNum; de++) {
                                        CreateOneTable(de, data);
                                    }
                                }
                                bindEvent(data)
                            }
                        }, 100));
                    }
                }
            },
            error: function () {
                console.log("数据传输失败");
                alert("获取数据失败，请尝试刷新页面");
            }
        });
    });

//    上传文件部分
    $("#fileUpload").on("change", function () {
        var $path = $(this).val();
        var $name = $path.substr($path.lastIndexOf(".") + 1); //获取后缀名
        if ($name != "xls") {
            $(".show-warm").css("display", "block");
            $("#fileBtn").attr("disabled", true)
                .removeClass("ui-state-default");
        }
        if ($name == "xls") {
            $(".show-warm").css("display", "none");
            $("#fileBtn").attr("disabled", false)
                .addClass("ui-state-default");
        }
    });
    //上传按钮事件
    $("#fileBtn").on("click", function () {
        var banMianVal = $("#banmian option:selected").val();
        var formData = new FormData();
        formData.append('programFile', $('#fileUpload')[0].files[0]);
        formData.append('boardType', banMianVal);
        $.ajax({
            url: "program/upload",
            type: "post",
            cache: false,
            data: formData,
            processData: false,
            contentType: false,
            success: function (data) {
                alert(data.result);

            },
            error: function () {
                console.log("数据传输失败！");
                alert("获取数据失败，请尝试刷新页面");
            }

        });
    });

//    生成表格部分
    firstTimeCreate();

    function firstTimeCreate() {
        $("#showWaiting").css("display", "block");
        $.ajax({
            url: "program/list",
            type: "post",
            dataType: "json",
            data: {
                ordBy: "state, create_time desc"
            },
            success: function (data) {
                $("#showWaiting").css("display", "none");
                autoCreateTable(data);
                bindEvent(data);
                completeAuto("stand-position", array1, fileCallBack);
                completeAuto("work-num", array2, workOrderCallBack);

                originNum = 100;
                newNum = 0;
                if (data.length > 100) {
                    $(window).on("scroll", debounce(function () {
                        var scrollTop = $(this).scrollTop();
                        var scrollHeight = $(document).height();
                        var windowHeight = $(this).height();
                        if (scrollTop + windowHeight >= scrollHeight - 20) {
                            newNum = originNum;
                            if (newNum < data.length) {
                                originNum += 100;
                                originNum = (originNum >= data.length ? data.length : originNum);  //判断加100后是否长度大于数据长度
                                for (var de = newNum; de < originNum; de++) {
                                    CreateOneTable(de, data);
                                }
                            }
                            bindEvent(data)
                        }
                    }, 100));
                }
            },
            error: function () {
                console.log("数据传输失败");
                alert("获取数据失败，请尝试刷新页面");
            }
        });
    }

//    动态生成表格
    function autoCreateTable(data) {
        $("#positionTable").empty();
        $("#stateModify").empty();
        var dataLength = data.length;     //获取数据长度
        for (var i = 0; i < dataLength; i++) {
            var $json = {};
            $json.label = data[i].fileName;
            array1.push($json);

            var $json1 = {};
            $json1.label = data[i].workOrder;
            array2.push($json1);
        }
        createOneTr(data);
    }

    //生成表格
    function createOneTr(data) {
        var originLength = data.length > 100 ? 100 : data.length;  //判断数据个数是否大于100，大于100个先加载100个
        for (var q = 0; q < originLength; q++) {
            var html = "";
            html += "<tr>";
            html += "<td>" + data[q].fileName + "</td>"
            html += "<td>" + data[q].workOrder + "</td>"
            html += "<td>" + data[q].boardTypeName + "</td>"
            html += "<td>" + data[q].createTimeString + "</td>"
            html += "<td>" + data[q].stateName + "</td>"
            html += "<td>" + data[q].line + "</td>"
            html += "<td>" + "<div class='commonDiv'><button id='" + q + "' class='ui-button ui-corner-all ui-state-default stateModifyCommon stateModifyBtn'>修改状态</button>" +
                "<select id='0" + q + "' class='stateModifyCommon stateModifySel'style='display: none'><option value=0>未开始</option><option value=1>进行中</option><option value=2>已完成</option> <option value=3>已作废</option></select>" +
                "<button id='00" + q + "' class='ui-button ui-corner-all ui-state-default stateModifyCommon stateModifyBtn' style='display: none'>取消</button>" +
                "<button id='000" + q + "' class='ui-button ui-corner-all ui-state-default stateModifyCommon stateModifyBtn'>修改表格</button>" + "</div>" + "</td>"
            html += "</tr>";
            $("#positionTable").append(html);
        }
    }

    //    动态生成一行表格
    function CreateOneTable(k, data) {
        var html = "";
        html += "<tr>";
        html += "<td>" + data[k].fileName + "</td>"
        html += "<td>" + data[k].workOrder + "</td>"
        html += "<td>" + data[k].boardTypeName + "</td>"
        html += "<td>" + data[k].createTimeString + "</td>"
        html += "<td>" + data[k].stateName + "</td>"
        html += "<td>" + data[k].line + "</td>"
        html += "<td>" + "<div class='commonDiv'><button id='" + k + "' class='ui-button ui-corner-all ui-state-default stateModifyCommon stateModifyBtn'>修改状态</button>" +
            "<select id='0" + k + "' class='stateModifyCommon stateModifySel'style='display: none'><option value=0>未开始</option><option value=1>进行中</option><option value=2>已完成</option> <option value=3>已作废</option></select>" +
            "<button id='00" + k + "' class='ui-button ui-corner-all ui-state-default stateModifyCommon stateModifyBtn' style='display: none'>取消</button>" +
            "<button id='000" + k + "' class='ui-button ui-corner-all ui-state-default stateModifyCommon stateModifyBtn'>修改表格</button>" + "</div>" + "</td>"
        html += "</tr>";
        $("#positionTable").append(html);
        var completeState = $("#positionTable td").eq(4 + 7 * k).text();
        if (completeState == "已完成" || completeState == "已作废") {
            $("#" + k).attr("disabled", "disabled")
                .removeClass("ui-state-default");
            $("#000" + k).attr("disabled", "disabled")
                .removeClass("ui-state-default");
        }
    }

    //            绑定事件
    function bindEvent(data) {
        var stateChange = "";    //用于显示
        var stateChange1 = 0;   //用于传递到后台
        var gdh = "";          //传递工单号到后台
        var modifyUrl = "";     //用于传递链接
        var originState = "";  //用于存储原先的状态
        var originStateVal = 0;  //用于比较是否允许进行保存
        var $id = "";
        for (var i = 0; i < $(".commonDiv").length; i++) {
            //修改按钮事件
            $("#" + i).hover(function () {
                $(this).addClass("ui-state-hover")
            }, function () {
                $(this).removeClass("ui-state-hover");
            })
                .unbind().on("click", function () {
                $id = $(this).attr("id");
                var $numId = parseInt($id);
                var dataId = data[$numId].id;
                if ($(this).text() == "修改状态") {
                    $(this).text("保存");
                    gdh = $("#positionTable td").eq(1 + 7 * $id).text();  //获取工单号
                    originState = $("#positionTable td").eq(4 + 7 * $id).text();  //获取原先状态

                    //将状态转换为可以作为对比条件的数字
                    switch (originState) {
                        case "未开始" :
                            originStateVal = 0;
                            break;
                        case "进行中" :
                            originStateVal = 1;
                            break;
                        case "已完成" :
                            originStateVal = 2;
                            break;
                    }
                    $("#0" + $id).css("display", "block");
                    $("#00" + $id).css("display", "block");
                    $("#000" + $id).css("display", "none");
                }
                else {
                    if (stateChange1 > originStateVal) {
                        //进行数据传输
                        $.ajax({
                            url: modifyUrl,
                            type: "post",
                            dataType: "json",
                            data: {
                                workOrder: gdh,
                                line: data[$id].line,
                                boardType: data[$id].boardType,
                                id: dataId
                            },
                            success: function (data) {
                                if (data.result == "succeed") {
                                    alert("修改状态成功");
                                    $("#" + $id).text("修改状态");
                                    $("#0" + $id).css("display", "none");
                                    $("#00" + $id).css("display", "none");
                                    $("#000" + $id).css("display", "block");
                                    $("#positionTable td").eq(4 + 7 * $id).text(stateChange);
                                    if (stateChange == "已作废" || stateChange == "已完成") {
                                        $("#" + $id).attr("disabled", "disabled")
                                            .removeClass("ui-state-default ui-state-hover");
                                        $("#000" + $id).attr("disabled", "disabled")
                                            .removeClass("ui-state-default ui-state-hover");
                                    }
                                }
                                else if (data.result == "failed_already_started") {
                                    alert("已存在相同并且正在进行的工单，请先完成该工单的相关操作。");
                                } else {
                                    alert("修改状态出错，请重新进行操作。")
                                }
                            },
                            error: function () {
                                console.log("数据传输失败");
                                alert("获取数据失败，请尝试刷新页面");
                            }
                        });
                    }
                    else {
                        alert("您选择的状态不符合，请重新选择！");
                    }
                }
            });
            var completeState = $("#positionTable td").eq(4 + 7 * i).text();
            if (completeState == "已完成" || completeState == "已作废") {
                $("#" + i).attr("disabled", "disabled")
                    .removeClass("ui-state-default");
                $("#000" + i).attr("disabled", "disabled")
                    .removeClass("ui-state-default");
            }
            //下拉框事件
            $("#0" + i).unbind().on("change", function () {
                var $thisId = $(this).attr("id");
                stateChange1 = parseInt($(this).val());    //获取修改后的状态对应的value值
                stateChange = $("#" + $thisId + " option:selected").text();
                switch (stateChange1) {                //传递链接
                    case 1 :
                        modifyUrl = "program/start";
                        break;
                    case 2 :
                        modifyUrl = "program/finish";
                        break;
                    case 3 :
                        modifyUrl = "program/cancel";
                        break;
                }
            });
            //取消按钮事件
            $("#00" + i).hover(function () {
                $(this).addClass("ui-state-hover")
            }, function () {
                $(this).removeClass("ui-state-hover");
            })
                .unbind().on("click", function () {
                var cancelId = parseInt($(this).attr("id"));
                $("#" + cancelId).text("修改状态");
                $("#0" + cancelId).css("display", "none");
                $(this).css("display", "none");
                $("#000" + $id).css("display", "block");
            });
            //修改报表
            $("#000" + i).hover(function () {
                $(this).addClass("ui-state-hover")
            }, function () {
                $(this).removeClass("ui-state-hover");
            })
                .unbind().on("click", function () {
                $(window).unbind();
                $id = $(this).attr("id");
                var $numId = parseInt($id);
                var dataId = data[$numId].id;
                var tableInfo = {
                    workOrder: data[$numId].workOrder,
                    lineNo: data[$numId].line,
                    boardType: data[$numId].boardTypeName
                };

                $.ajax({
                    url: "program/listItem",
                    type: "post",
                    dataType: "json",
                    data: {
                        id: dataId
                    },
                    success: function (data) {
                        $("#position-table").css("display", "none");
                        $("#edit-table").css("display", "block");
                        insertEditTable(data, tableInfo);
                        initEditTable(dataId);
                    },
                    error: function () {
                        console.log("数据传输失败");
                        alert("获取数据失败，请尝试刷新页面");
                    }
                });
            });


        }
    }

//   自动补全函数
    function completeAuto(id, array, fn) {
        $("#" + id).autocompleter({
            highlightMatches: true,
            source: array,
            template: '{{ label }}',
            empty: false,
            limit: 5,
            callback: function (index, value, selected) {
                fn(selected.label);
            }
        });
    }

//    站位表回调函数
    function fileCallBack(a) {
        $(window).unbind();
        var workNum = $("#work-num").val() == "" ? null : $("#work-num").val();     //工单的输入内容  workOrder
        var state = $("#state option:selected").val();   //状态  state
        var line = $("#line-num option:selected").text() == "不限" ? null : $("#line-num option:selected").text();  //线号 line
        $.ajax({
            url: "program/list",
            type: "post",
            dataType: "json",
            data: {
                fileName: a,
                workOrder: workNum,
                state: state,
                line: line
            },
            success: function (data) {
                autoCreateTable(data);
                bindEvent(data);
            },
            error: function () {
                console.log("数据传输失败");
                alert("获取数据失败，请尝试刷新页面");
            }
        });
    }

//    工单回调函数
    function workOrderCallBack(a) {
        $(window).unbind();
        var standPosition = $("#stand-position").val() == "" ? null : $("#stand-position").val();     //站位的输入内容   对应fileName
        var state = $("#state option:selected").val();   //状态  state
        var line = $("#line-num option:selected").text() == "不限" ? null : $("#line-num option:selected").text();  //线号 line
        $.ajax({
            url: "program/list",
            type: "post",
            dataType: "json",
            data: {
                fileName: standPosition,
                workOrder: a,
                state: state,
                line: line
            },
            success: function (data) {
                autoCreateTable(data);
                bindEvent(data);
            },
            error: function () {
                console.log("数据传输失败");
                alert("获取数据失败，请尝试刷新页面");
            }
        });
    }

    //编辑表格获取数据
    function insertEditTable(json, tableInfo) {
        console.log(json)
        //填充最后一行空的
        json.push({
            alternative: "",
            isAlternative: "",
            lineseat: "",
            materialNo: "",
            position: "",
            programId: "",
            quantity: "",
            specitification: ""

        });

        //表头
        $(".table-header p").eq(0).text("工单号：" + tableInfo.workOrder);
        $(".table-header p").eq(1).text("版面：" + tableInfo.boardType);
        $(".table-header p").eq(2).text("线号：" + tableInfo.lineNo);


        //表单填充
        var table = document.getElementById('SMTTable');
        for (var i = 0; i < json.length; i++) {
            var row = table.insertRow(table.rows.length);

            var serialNo = row.insertCell(0);
            if (json[i].serialNo === null) {
                serialNo.innerHTML = "";
            } else if (json[i].serialNo === undefined) {
                serialNo.innerHTML = "";
            } else {
                serialNo.innerHTML = json[i].serialNo.toString();
            }

            var lineSeat = row.insertCell(1);
            lineSeat.innerHTML = json[i].lineseat || "";

            var materialNo = row.insertCell(2);
            materialNo.innerHTML = json[i].materialNo || "";

            var quantity = row.insertCell(3);
            if (json[i].quantity === null) {
                quantity.innerHTML = "0";
            } else {
                quantity.innerHTML = json[i].quantity.toString() || "";
            }

            var spec = row.insertCell(4);
            spec.innerHTML = json[i].specitification || "";

            var position = row.insertCell(5);
            position.innerHTML = json[i].position || "";

            var alter = row.insertCell(6);
            if (json[i].alternative === true) {
                alter.innerHTML = "<input type=\"checkbox\" checked='checked' disabled/>";
            } else if (json[i].alternative === false) {
                alter.innerHTML = "<input type=\"checkbox\" disabled/>";
            } else if (json[i].alternative === "") {
                alter.innerHTML = "";
            }

            var operation = row.insertCell(7);
            operation.innerHTML = "";

        }

    }

    //编辑表格初始化
    function initEditTable(currentId) {
        $(".smt-back a").on("click", function (e) {
            e.stopPropagation();
            if (editStack.length !== 0) {
                var isConfirm = confirm("您还有未保存的数据，确定要退出吗？")
                if (isConfirm) {
                    exitEdit();
                }

            } else {
                exitEdit();
            }
        });
        $("#save-btn").unbind("click").click(function (e) {
            e.stopPropagation();
            if (editStack.length !== 0) {
                $.ajax({
                    url: "program/updateItem",
                    type: "post",
                    dataType: "json",
                    data: {
                        operations: JSON.stringify(editStack)
                    },
                    beforeSend: function () {
                        $("#showWaiting").css("display", "block");
                    },
                    success: function (data) {
                        if (data.result === "succeed") {
                            console.log(data);
                            alert("保存成功")
                            console.log(editStack);
                            editStack = [];
                            $("#showWaiting").css("display", "none");
                            exitEdit();
                        } else {
                            alert("未知错误，请联系我们")
                        }
                    },
                    error: function (err) {
                        console.log(err);
                        alert("未知错误，请重试")
                    }
                });
            }
            return false;
        });
        //$('.edit').handleTable({"cancel" : "<span class='glyphicon glyphicon-remove'></span>"});
        $('.editable').handleTable({
            "handleFirst": true,
            "cancel": [" <span class='icon-cancel'></span> ", "取消"],
            "edit": [" <span class='icon-edit'></span> ", "编辑该行"],
            "add": [" <span class='icon-add'></span> ", "添加一行"],
            "del": [" <span class='icon-del'></span> ", "删除该行"],
            "save": [" <span class='icon-confirm'></span> ", "保存"],
            "confirm": [" <span class='icon-confirm'></span> ", "确定"],
            "operatePos": -1,
            "editableCols": [0, 1, 2, 3, 4, 5],
            "order": ["add", "edit", "del"],
            "saveCallback": function (data, isSuccess, etcData) { //这里可以写ajax内容，用于保存编辑后的内容
                //data: 返回的数据
                //isSucess: 方法，用于保存数据成功后，将可编辑状态变为不可编辑状态
                var flag = false; //ajax请求成功（保存数据成功），才回调isSuccess函数（修改保存状态为编辑状态）
                for (var i = 0; i < 6; i++) {
                    if (data[i] !== etcData.beforeCol.children().eq(i).text()) {
                        flag = true;
                    }
                }
                if (data[6] !== etcData.beforeCol.children().eq(6).children().is(":checked")) {
                    flag = true;
                }
                if (flag) {
                    isSuccess();
                    if (data[1].slice()[1] === "-") {
                        data[1] = "0" + data[0]
                    }
                    editStack.push({
                        "serialNo": data[0],
                        "operation": 1,
                        "targetLineseat": etcData.beforeCol.children().eq(1).text(),
                        "targetMaterialNo": etcData.beforeCol.children().eq(2).text(),
                        "programId": currentId,
                        "lineseat": data[1],
                        "alternative": data[6],
                        "materialNo": data[2],
                        "specitification": data[4],
                        "position": data[5],
                        "quantity": Number(data[3])

                    });
                } else {
                    alert("请确认内容有变动后才可保存，\n若想取消编辑请点击取消按钮");
                }
                console.log(editStack);
                return true;
            },
            "addCallback": function (data, isSuccess, etcData) {
                var flag = true;
                for (var i = 0; i < 6; i++) {
                    if (data[i] === "") {
                        flag = false;
                    }
                }
                if (flag) {
                    isSuccess();
                    if (data[1].slice()[1] === "-") {
                        data[1] = "0" + data[1]
                    }
                    editStack.push({
                        "serialNo": data[0],
                        "operation": 0,
                        "targetLineseat": etcData.nextCol.children().eq(1).text(),
                        "targetMaterialNo": etcData.nextCol.children().eq(2).text(),
                        "programId": currentId,
                        "lineseat": data[1],
                        "alternative": data[6],
                        "materialNo": data[2],
                        "specitification": data[4],
                        "position": data[5],
                        "quantity": Number(data[3])

                    });
                } else {
                    alert("添加数据失败，请检查数据格式\n文本框不能为空");
                }
            },
            "delCallback": function (isSuccess, etcData) {
                var flag = confirm("确定要删除该行吗？保存后不可更改");
                if (flag) {
                    editStack.push({
                        "serialNo": "",
                        "operation": 2,
                        "targetLineseat": etcData.thisCol.children().eq(1).text(),
                        "targetMaterialNo": etcData.thisCol.children().eq(2).text(),
                        "programId": currentId,
                        "lineseat": "",
                        "alternative": "",
                        "materialNo": "",
                        "specitification": "",
                        "position": "",
                        "quantity": ""
                    });
                    isSuccess();
                }
            }
        });

    }

    function exitEdit() {
        $(".smt-table tr td").remove();
        $("#edit-table").css("display", "none");
        $("#position-table").css("display", "block");
        editStack = [];
        window.location.reload()
    }

});

//函数防抖
function debounce(func, wait, immediate) {
    var timeout;
    return function () {
        var context = this, args = arguments;
        var later = function () {
            timeout = null;
            if (!immediate) func.apply(context, args);
        };
        var callNow = immediate && !timeout;
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
        if (callNow) func.apply(context, args);
    };
}

// {
//     alternative:false,
//     lineseat:"09-34",
//     materialNo:"01.80.0333",
//     operation:1,
//     position:"U121",
//     programId:"0fdf7aa800f0412285e34785c198bfa1",
//     quantity:1,
//     specitification:"01.80.0333; & ;CMOS?????-???; & ;CMOS???,JX-H62,CSP,????1/4",
//     targetLineseat:"09-34",
//     targetMaterialNo:"01.80.0333"
// }
