<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Server Details</title>
</head>
<body>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>
<script>


$(function () {
    function callServlet() {
    	//var first = $("#first").val(); //document.getElementById('number1').value;
        //var last = $("#last").val(); //document.getElementById('number2').value;
        //var sid = $("#sid").val(); //document.getElementById('number2').value;
        var d = new Date();
        var timeStamp = d.toUTCString();
        var deviceId ="258D9EA6-4F9E-4DCD-9CAD-1A2B3E0A4D0E";
        console.log(timeStamp);
        var myData = {
                "device_id": deviceId ,
                "timeIn": "2016-05-09 02:56:33",
                "firstTimeIn": "2016-05-09 02:56:33"
                //,
                //"checked_in": "true"
        };
        console.log(myData);
        //var url_post = 'http://localhost:8080/AutomaticClassAttendanceSystem/sensor/class/section/' + deviceId;
        var url_post = "http://ec2-54-191-40-122.us-west-2.compute.amazonaws.com:8080/AutomaticClassAttendanceSystem/sensor/class/section/258D9EA6-4F9E-4DCD-9CAD-1A2B3E0A4D0E";
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
    
    function callServlet2() {
    	//var first = $("#first").val(); //document.getElementById('number1').value;
        //var last = $("#last").val(); //document.getElementById('number2').value;
        //var sid = $("#sid").val(); //document.getElementById('number2').value;
        var d = new Date();
        var timeStamp = d.toUTCString();
        var deviceId ="258D9EA6-4F9E-4DCD-9CAD-1A2B3E0A7WWW";
        console.log(timeStamp);
        var myData = {
                "device_id": deviceId ,
                "timeIn": "2016-05-09 02:56:33"
                //,
                //"checked_in": "true"
        };
        console.log(myData);
        var url_post = 'http://localhost:8080/AutomaticClassAttendanceSystem/sensor/class/section/' + deviceId;
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
    
    function callPutTimeInServlet() {
    	//var first = $("#first").val(); //document.getElementById('number1').value;
        //var last = $("#last").val(); //document.getElementById('number2').value;
        //var sid = $("#sid").val(); //document.getElementById('number2').value;
        var d = new Date();
        var timeStamp = d.toUTCString();
        var deviceId ="258D9EA6-4F9E-4DCD-9CAD-1A2B3E0A4D0E";
        console.log(timeStamp);
        var myData = {
                "device_id": deviceId ,
                "timeIn": "2016-05-09 10:09:33"
                //,
                //"checked_in": "true"
        };
        console.log(myData);
        var url_post = 'http://localhost:8080/AutomaticClassAttendanceSystem/sensor/class/section/' + deviceId + "/timeIn";
        console.log(url_post);
        $.ajax({
            type: "PUT",
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
    
    function callPutTimeOutServlet() {
    	//var first = $("#first").val(); //document.getElementById('number1').value;
        //var last = $("#last").val(); //document.getElementById('number2').value;
        //var sid = $("#sid").val(); //document.getElementById('number2').value;
        var d = new Date();
        var timeStamp = d.toUTCString();
        var deviceId ="258D9EA6-4F9E-4DCD-9CAD-1A2B3E0A4D0E";
        console.log(timeStamp);
        var myData = {
                "device_id": deviceId ,
                "timeOut": "2016-05-09 11:01:33"
                //"checked_in": "false"
        };
        console.log(myData);
        var url_post = 'http://localhost:8080/AutomaticClassAttendanceSystem/sensor/class/section/' + deviceId + "/timeOut";
        console.log(url_post);
        $.ajax({
            type: "PUT",
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
            url: "http://localhost:8080/AutomaticClassAttendanceSystem/sensor/class/section/258D9EA6-4F9E-4DCD-9CAD-1A2B3E0A4D0E", //"/AjaxServletCalculator",
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
    $('#calc2Btn').click(function() {
        callServlet();
    });
    $('#DelBtn').click(function() {
        callDeleteServlet();
    });
    $('#putTimeInBtn').click(function() {
        callPutTimeInServlet();
    });
    $('#putTimeOutBtn').click(function() {
        callPutTimeOutServlet();
    });
});
</script>
<h3>Please enter a number to Square : </h3>

<input type="button" id="calcBtn" value="Calc">
<input style="font-family: cursive; border:none" type="text" id="result" />
<input style="font-family: cursive; border:none; width: 100%" type="text" value="" id="resultText" />

<input type="button" id="calc2Btn" value="Calc2">
<input style="font-family: cursive; border:none" type="text" id="result" />
<input style="font-family: cursive; border:none; width: 100%" type="text" value="" id="resultText" />

<h3>Delete User : </h3>

<input type="button" id="DelBtn" value="Delete">

<h3>Put TimeIn : </h3>

<input type="button" id="putTimeInBtn" value="Put TimeIn">

<h3>Put TimeOut : </h3>

<input type="button" id="putTimeOutBtn" value="Put TimeOut">
</body>
</html>