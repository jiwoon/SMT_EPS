$(function(){function sendData(t,a){$("#showWaiting").css("display","block"),$.ajax({url:"MPShow/show",type:"post",dataType:"json",data:{startTime:t,endTime:a},success:function(t){$("#showWaiting").css("display","none"),lineArray(t),setColor()},error:function(){console.log("数据传输出错了！！")}})}function createTable(t){for(var a=0;a<t.length;a++){var e="";e+="<tr>",e+="<td rowspan='6'>"+t[a].line+"</td>",e+="</tr>",e+="<tr>",e+="<td></td>",e+="<td>成功率</td>",e+="<td>失败率</td>",e+="</tr>",e+="<tr>",e+="<td>上料</td>",e+="<td>"+t[a].feedSuc+"</td>",e+="<td>"+t[a].feedFail+"</td>",e+="</tr>",e+="<tr>",e+="<td>换料</td>",e+="<td>"+t[a].changeSuc+"</td>",e+="<td>"+t[a].changeFail+"</td>",e+="</tr>",e+="<tr>",e+="<td>抽检</td>",e+="<td>"+t[a].someSuc+"</td>",e+="<td>"+t[a].someFail+"</td>",e+="</tr>",e+="<tr>",e+="<td>全检</td>",e+="<td>"+t[a].allsSuc+"</td>",e+="<td>"+t[a].allsFail+"</td>",e+="</tr>",$("#wechat_tbody").append(e)}}function lineArray(data){for(var arr=[],i=0;i<data.length;i++)if(0==arr.length)""!=data[i].line&&(arr.push(data[i].line),eval("var $"+data[i].line+" = []"),eval("$"+data[i].line).push(data[i]),createTable(eval("$"+data[i].line)));else{for(var j=0;j<arr.length&&data[i].line!=arr[j];j++);""!=data[i].line&&(arr.push(data[i].line),eval("var $"+data[i].line+" = []"),eval("$"+data[i].line).push(data[i]),createTable(eval("$"+data[i].line)))}}function setColor(){for(var t=$("#wechat_tbody tr"),a=parseInt(t.length/6),e=0;e<a;e++)for(var r=1;r<6;r++)$(t[r+6*e].children[1]).css("color","green"),$(t[r+6*e].children[2]).css("color","red")}function checkType(t){var a=[];switch(t){case 0:a=timeCalculate(1);break;case 1:a=timeCalculate(2);break;case 2:a=timeCalculate(7);break;case 3:a=timeCalculate()}return a}function timeCalculate(t){var a,e,r,i,l=[];r=!!t,t=t||2;var d=new Date,n=d.getFullYear(),s=d.getMonth()+1,o=d.getDate();a=2==t&&r?n+"-"+s+"-"+o+" 00:00:00":n+"-"+s+"-"+o+" 23:59:59",l.push(a),i=r?d.getTime()-864e5*(t-1):d.getTime()-864e5*(t-1)*(o-1);var c=new Date(i);return e=c.getFullYear()+"-"+(c.getMonth()+1)+"-"+c.getDate()+" 00:00:00",l.push(e),l}var passUrl=window.location.href,passType=passUrl.substring(passUrl.lastIndexOf("=")+1),arr=[],flag=0,startTime,endTime;"0"===passType?(flag=0,$("#timeShow").text("当天数据统计")):"1"===passType?(flag=1,$("#timeShow").text("前一天数据统计")):"2"===passType?(flag=2,$("#timeShow").text("七天内数据统计")):"3"===passType&&(flag=3,$("#timeShow").text("本月数据统计")),arr=checkType(flag),startTime=arr[1],endTime=arr[0],sendData(startTime,endTime)});