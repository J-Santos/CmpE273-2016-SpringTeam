<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Sensor Data Collector</title>
</head>
<body>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>
<script>


$(function () {
    function callServlet() {
    	var first = $("#first").val(); //document.getElementById('number1').value;
        var last = $("#last").val(); //document.getElementById('number2').value;
        var sid = $("#sid").val(); //document.getElementById('number2').value;
        var myData = {
                "first": first,
                "last": last,
                "sid": sid,
                "email": "example@example.com"
        };
        console.log(myData);
        var url_post = 'http://localhost:8080/AutomaticClassAttendanceSystem/sensor/class/student/' + sid;
        console.log(url_post);
        $.ajax({
            type: "POST",
            url: url_post,  //"/AjaxServletCalculator",
            contentType : 'application/json; charset=utf-8',
            data: {
                jsonData: JSON.stringify(myData) // this works but mocky.io doesn't support it
            },
            dataType: "json",

            //if received a response from the server
            success: function (json) {
                //our country code was correct so we have some information to display
                console.log(json);
                //var json = JSON.parse(data);
                alert(json.data);
                /*document.getElementById('number1').value = data.*/
            }
        });
    }
    
    function callDeleteServlet() {
        $.ajax({
            type: "DELETE",
            url: "http://localhost:8080/AutomaticClassAttendanceSystem/sensor/class/student/1234", //"/AjaxServletCalculator",
            contentType : 'application/json; charset=utf-8',
           /*  data: {
                jsonData: JSON.stringify(myData) // this works but mocky.io doesn't support it
            }, */
            //dataType: "json",

            //if received a response from the server
            success: function (json) {
                //our country code was correct so we have some information to display
                console.log(json);
                //var json = JSON.parse(data);
                //alert(json.data);
                //document.getElementById('number1').value = data
            } 
            
        });
    }
    
    $('#calcBtn').click(function() {
        callServlet();
    });
    $('#DelBtn').click(function() {
        callDeleteServlet();
    });
});
</script>
<h3>Please enter a number to Square : </h3>

<input style="width: 33px; margin-left: 2px; " type="text" id="first" name="first">
<input style="width: 33px; margin-left: 2px;" type="text" id="last" name="last">
<input style="width: 33px; margin-left: 2px; " type="text" id="sid" name="sid">
<input type="button" id="calcBtn" value="Calc">
<input style="font-family: cursive; border:none" type="text" id="result" />
<input style="font-family: cursive; border:none; width: 100%" type="text" value="" id="resultText" />

<h3>Delete User : </h3>

<input type="button" id="DelBtn" value="Delete">
window.location.pathname
</body>
</html>