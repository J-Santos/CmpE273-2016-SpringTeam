//
//  ViewController.swift
//  UIWebView Example
//
//  Created by Jelson Santos on 5/7/16.
//  Copyright © 2016 Jelson Santos. All rights reserved.
//

import CoreBluetooth
import UIKit
import Foundation


class ViewController: UIViewController, UIWebViewDelegate, CBCentralManagerDelegate {
    
    @IBOutlet var webView: UIWebView!
    @IBOutlet var activityIndicator: UIActivityIndicatorView!
    //@IBOutlet weak var tableView: UITableView!
    
    var centralManager: CBCentralManager?
    var peripherals: Array<CBPeripheral> = Array<CBPeripheral>()
    var data = [String]()
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        centralManager = CBCentralManager(delegate: self, queue: dispatch_get_main_queue())
        
        webView.scalesPageToFit = true;
        // Do any additional setup after loading the view, typically from a nib.
        let localfilePath = NSBundle.mainBundle().URLForResource("index", withExtension: "html");
        let urlString: String = localfilePath!.absoluteString
        let id = UIDevice.currentDevice().identifierForVendor!.UUIDString
        var urlReq = urlString + "?udid="
        urlReq = urlReq + id
        let localfilePathUrl = NSURL(string: urlReq)
        let myRequest = NSURLRequest(URL: localfilePathUrl!);
        webView.loadRequest(myRequest);
        webView.frame=self.view.bounds;
    }
    
    //CoreBluetooth methods
    func centralManagerDidUpdateState(central: CBCentralManager)
    {
        if (central.state == CBCentralManagerState.PoweredOn)
        {
            print("BLE is on")
            //let CBCentralManagerScanOptionAllowDuplicatesKey: String
            //let name = "AcademicSystem"
            self.centralManager?.scanForPeripheralsWithServices(nil, options: [CBCentralManagerScanOptionAllowDuplicatesKey: true])
            //print("BLE is on")
        }
        else
        {
            // do something like alert the user that ble is not on
            let date = NSDate();
            let formatter = NSDateFormatter()
            formatter.dateFormat = "yyyy-MM-dd HH:mm:ss";
            _ = formatter.stringFromDate(date)
            formatter.timeZone = NSTimeZone(abbreviation: "PST")
            let utcTimeZoneStr = formatter.stringFromDate(date)
            for peripheral in peripherals {
                self.centralManager?.cancelPeripheralConnection(peripheral)
                print("Peripheral disconnected: " + peripheral.name! + "at " + utcTimeZoneStr)
            }
            let device_id = UIDevice.currentDevice().identifierForVendor!.UUIDString
            
            let put_url = "http://ec2-54-191-40-122.us-west-2.compute.amazonaws.com:8080/AutomaticClassAttendanceSystem/sensor/class/section/258D9EA6-4F9E-4DCD-9CAD-1A2B3E0A4D0E/timeOut"
            let exist = doesItExist(put_url, http_code: 200)
            
            if(exist){
                //print("It exist")
                //let request = NSMutableURLRequest(URL: NSURL(string: post_url)!)
                // create some JSON data and configure the request
                let jsonFirst = "myData = {\"device_id\":\""
                let jsonThird = "\",\"timeOut\":\""
                let jsonFifth = "\"}"
                let jsonString = jsonFirst+device_id+jsonThird+utcTimeZoneStr+jsonFifth
                makePutRequest(put_url, jsonString: jsonString)
            }
            peripherals.removeAll()
            //print("SIZE: ", peripherals.count)
            let alert = UIAlertController(title: "Bluetooth is Off!", message:"Please turn your bluetooth on.", preferredStyle: .Alert)
            alert.addAction(UIAlertAction(title: "OK", style: .Default) { _ in })
            self.presentViewController(alert, animated: true){}
        }
    }
    
    func centralManager(central: CBCentralManager, didDiscoverPeripheral peripheral: CBPeripheral, advertisementData: [String : AnyObject], RSSI: NSNumber)
    {
        //print(advertisementData["kCBAdvDataLocalName"])
        //        print(peripheral)
        let a = advertisementData["kCBAdvDataLocalName"] as? String
        
        //The name of simble Device is WRL-13632
        if a == "WRL-13632" && a != nil{
            //print(advertisementData)
            //print(peripheral)
            centralManager!.connectPeripheral(peripheral, options:nil)
            //sleep(10)
            let id = UIDevice.currentDevice().identifierForVendor!.UUIDString
            print("Device ID: "+id)
            
            peripherals.append(peripheral)
            
        }
    }
    
    func doesItExist(url: String, http_code: Int) -> Bool {
        var output = true
        let request = NSMutableURLRequest(URL: NSURL(string: url)!)
        request.HTTPMethod = "GET"
        //let postString = "id=13&name=Jack"
        //request.HTTPBody = postString.dataUsingEncoding(NSUTF8StringEncoding)
        let task = NSURLSession.sharedSession().dataTaskWithRequest(request) { data, response, error in
            guard error == nil && data != nil else {                                                          // check for fundamental networking error
                print("error=\(error)")
                return
            }
            
            if let httpStatus = response as? NSHTTPURLResponse where httpStatus.statusCode != http_code {           // check for http errors
                //print("statusCode EXIST should be 200, but is \(httpStatus.statusCode) http_code is \(http_code)")
                print("response = \(response)")
                output = false
            }
            
            let responseString = NSString(data: data!, encoding: NSUTF8StringEncoding)
            print("responseString = \(responseString)")
        }
        task.resume()
        
        return output
        
    }
    
    func centralManager(central: CBCentralManager,
                        didConnectPeripheral peripheral: CBPeripheral) {
        let date = NSDate();
        let formatter = NSDateFormatter();
        formatter.dateFormat = "yyyy-MM-dd HH:mm:ss";
        _ = formatter.stringFromDate(date);
        formatter.timeZone = NSTimeZone(abbreviation: "PST");
        let utcTimeZoneStr = formatter.stringFromDate(date);
        print("Peripheral connected: " + peripheral.name! + " at " + utcTimeZoneStr)
        let device_id = UIDevice.currentDevice().identifierForVendor!.UUIDString
        let get_url = "http://ec2-54-191-40-122.us-west-2.compute.amazonaws.com:8080/AutomaticClassAttendanceSystem/sensor/class/section/"+device_id
        
        
        let post_exist = doesItExist(get_url, http_code: 404)
        let put_exist = doesItExist(get_url, http_code: 200)
        if(put_exist){
            // create some JSON data and configure the request
            let put_url = "http://ec2-54-191-40-122.us-west-2.compute.amazonaws.com:8080/AutomaticClassAttendanceSystem/sensor/class/section/"+device_id+"/timeIn"
            let jsonFirst = "myData = {\"device_id\":\""
            let jsonThird = "\",\"timeIn\":\""
            let jsonFifth = "\"}"
            let jsonString = jsonFirst+device_id+jsonThird+utcTimeZoneStr+jsonFifth
            makePutRequest(put_url, jsonString: jsonString)
            
        }
        if(post_exist){
            let jsonFirst = "myData = {\"device_id\":\""
            let jsonThird = "\",\"timeIn\":\""
            let jsont = "\",\"firstTimeIn\":\""
            let jsonFifth = "\"}"
            let jsonString = jsonFirst+device_id+jsonThird+utcTimeZoneStr+jsont+utcTimeZoneStr+jsonFifth
            makePostRequest(get_url, jsonString: jsonString)
        }
        
    }
    
    func makePostRequest(url: String, jsonString: String){
        let request = NSMutableURLRequest(URL: NSURL(string: url)!)
        //var output = true
        //print("jsonString: " + jsonString)
        request.HTTPBody = jsonString.dataUsingEncoding(NSUTF8StringEncoding, allowLossyConversion: true)
        request.HTTPMethod = "POST"
        request.setValue("application/json; charset=utf-8", forHTTPHeaderField: "Content-Type")
        
        let task = NSURLSession.sharedSession().dataTaskWithRequest(request) { data, response, error in
            guard error == nil && data != nil else {                                                          // check for fundamental networking error
                print("error=\(error)")
                return
            }
            
            if let httpStatus = response as? NSHTTPURLResponse where httpStatus.statusCode != 200 {           // check for http errors
                //print("statusCode should be 201, but is \(httpStatus.statusCode)")
                print("response = \(response)")
                //output = false
            }
            
            let responseString = NSString(data: data!, encoding: NSUTF8StringEncoding)
            print("responseString = \(responseString)")
        }
        task.resume()
    }
    
    func makePutRequest(url: String, jsonString: String){
        let request = NSMutableURLRequest(URL: NSURL(string: url)!)
        //print("jsonString: " + jsonString)
        request.HTTPBody = jsonString.dataUsingEncoding(NSUTF8StringEncoding, allowLossyConversion: true)
        request.HTTPMethod = "PUT"
        request.setValue("application/json; charset=utf-8", forHTTPHeaderField: "Content-Type")
        
        let task = NSURLSession.sharedSession().dataTaskWithRequest(request) { data, response, error in
            guard error == nil && data != nil else {                                                          // check for fundamental networking error
                print("error=\(error)")
                return
            }
            
            if let httpStatus = response as? NSHTTPURLResponse where httpStatus.statusCode != 201 {           // check for http errors
                //print("statusCode should be 201, but is \(httpStatus.statusCode)")
                print("response = \(response)")
            }
            
            let responseString = NSString(data: data!, encoding: NSUTF8StringEncoding)
            print("responseString = \(responseString)")
        }
        task.resume()
    }
    
    func centralManager(central: CBCentralManager,
                        didDisconnectPeripheral peripheral: CBPeripheral,
                                                error: NSError?) {
        let date = NSDate();
        let formatter = NSDateFormatter();
        formatter.dateFormat = "yyyy-MM-dd HH:mm:ss";
        _ = formatter.stringFromDate(date);
        formatter.timeZone = NSTimeZone(abbreviation: "PST");
        let utcTimeZoneStr = formatter.stringFromDate(date);
        print("Peripheral disconnected: " + peripheral.name! + "at " + utcTimeZoneStr)
        let device_id = UIDevice.currentDevice().identifierForVendor!.UUIDString
        
        let put_url = "http://ec2-54-191-40-122.us-west-2.compute.amazonaws.com:8080/AutomaticClassAttendanceSystem/sensor/class/section/258D9EA6-4F9E-4DCD-9CAD-1A2B3E0A4D0E/timeOut"
        
        let exist = doesItExist(put_url, http_code: 200)
        
        if(exist){
            //print("It exist")
            //let request = NSMutableURLRequest(URL: NSURL(string: post_url)!)
            // create some JSON data and configure the request
            let jsonFirst = "myData = {\"device_id\":\""
            let jsonThird = "\",\"timeOut\":\""
            let jsonFifth = "\"}"
            let jsonString = jsonFirst+device_id+jsonThird+utcTimeZoneStr+jsonFifth
            makePutRequest(put_url, jsonString: jsonString)
        }
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    override func viewDidAppear(animated: Bool) {
        webView.frame = UIScreen.mainScreen().bounds
        webView.center = self.view.center
        webView.frame=self.view.bounds;
    }
    @IBAction func doRefresh(_: AnyObject) {
        webView.frame = UIScreen.mainScreen().bounds
        webView.center = self.view.center
        webView.scalesPageToFit = true;
        webView.frame=self.view.bounds;
        webView.reload()
    }
    
    @IBAction func goBack(_: AnyObject) {
        webView.frame = UIScreen.mainScreen().bounds
        webView.center = self.view.center
        webView.scalesPageToFit = true;
        webView.frame=self.view.bounds;
        webView.goBack()
    }
    
    @IBAction func goForward(_: AnyObject) {
        webView.frame = UIScreen.mainScreen().bounds
        webView.center = self.view.center
        webView.scalesPageToFit = true;
        webView.frame=self.view.bounds;
        webView.goForward()
    }
    
    @IBAction func stop(_: AnyObject) {
        webView.scalesPageToFit = true;
        webView.stopLoading()
    }
    
}

