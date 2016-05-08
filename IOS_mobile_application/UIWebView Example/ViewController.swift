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
    //var advertisingData: Array<String> = Array<String>()
    //var advertisingData: [String] = []
    var data = [String]()
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        centralManager = CBCentralManager(delegate: self, queue: dispatch_get_main_queue())
        
        webView.scalesPageToFit = true;
        // Do any additional setup after loading the view, typically from a nib.
        let localfilePath = NSBundle.mainBundle().URLForResource("index", withExtension: "html");
        let myRequest = NSURLRequest(URL: localfilePath!);
        webView.loadRequest(myRequest);
        webView.frame=self.view.bounds;
        
        
        
        //        let localfilePath = NSBundle.mainBundle().pathForResource("index", ofType: "html", inDirectory: "www");
        //        let nsurl = NSURL(fileURLWithPath: localfilePath!)
        //
        //        let req = NSURLRequest(URL: nsurl)
        //        webView.loadRequest(req);
        //        self.view.addSubview(webView)
        
    }
    
    
    
    
    //CoreBluetooth methods
    func centralManagerDidUpdateState(central: CBCentralManager)
    {
        if (central.state == CBCentralManagerState.PoweredOn)
        {
            print("BLE is on")
            //let CBCentralManagerScanOptionAllowDuplicatesKey: String
            //let name = "AcademicSystem"
            self.centralManager?.scanForPeripheralsWithServices(nil, options: [CBCentralManagerScanOptionAllowDuplicatesKey: false])
            //print("BLE is on")
        }
        else
        {
            // do something like alert the user that ble is not on
            let date = NSDate();
            let formatter = NSDateFormatter()
            formatter.dateFormat = "yyyy-MM-dd HH:mm:ss ZZZ";
            _ = formatter.stringFromDate(date)
            formatter.timeZone = NSTimeZone(abbreviation: "UTC")
            let utcTimeZoneStr = formatter.stringFromDate(date)
            for peripheral in peripherals {
                self.centralManager?.cancelPeripheralConnection(peripheral)
                print("Peripheral disconnected: " + peripheral.name! + "at " + utcTimeZoneStr)
            }
            peripherals.removeAll()
            //print("SIZE: ", peripherals.count)
            let alert = UIAlertController(title: "Oops!", message:"This feature isn't available right now", preferredStyle: .Alert)
            alert.addAction(UIAlertAction(title: "OK", style: .Default) { _ in })
            self.presentViewController(alert, animated: true){}
        }
    }
    
    func centralManager(central: CBCentralManager, didDiscoverPeripheral peripheral: CBPeripheral, advertisementData: [String : AnyObject], RSSI: NSNumber)
    {
        //        print(advertisementData)
        //        print(peripheral)
        let a = advertisementData["kCBAdvDataLocalName"] as? String
        if a == "AcademicSystem" && a != nil{
            //print(advertisementData)
            //print(peripheral)
            centralManager!.connectPeripheral(peripheral, options:nil)
            //sleep(10)
            //print("UUID: "+ peripheral.UUI)
            //UUID: 5CD07F08-3765-40DA-88D0-5BC6A8F66260
            //UIDevice.currentDevice().identifierForVendor.UUIDString
            let id = UIDevice.currentDevice().identifierForVendor!.UUIDString

            print("Device ID: "+id)
            let uuid=NSUUID().UUIDString
            print("UUID: "+uuid)
            print("Yes, it's in class")
            // Set the URL where we're making the request
            //let request = NSURLRequest(URL: NSURL(string: "http://iswift.org")!)
            let request = NSMutableURLRequest(URL: NSURL(string: "http://httpbin.org/ip")!)
            request.HTTPMethod = "GET"
            //let postString = "id=13&name=Jack"
            //request.HTTPBody = postString.dataUsingEncoding(NSUTF8StringEncoding)
            let task = NSURLSession.sharedSession().dataTaskWithRequest(request) { data, response, error in
                guard error == nil && data != nil else {                                                          // check for fundamental networking error
                    print("error=\(error)")
                    return
                }
                
                if let httpStatus = response as? NSHTTPURLResponse where httpStatus.statusCode != 200 {           // check for http errors
                    print("statusCode should be 200, but is \(httpStatus.statusCode)")
                    print("response = \(response)")
                }
                
                let responseString = NSString(data: data!, encoding: NSUTF8StringEncoding)
                print("responseString = \(responseString)")
            }
            task.resume()
            //print(peripheral)
            peripherals.append(peripheral)
            
        }
        //print(advertisementData["kCBAdvDataLocalName"])
        //print(advertisementData["kCBAdvDataTxPowerLevel"] as? String)
        //let b = advertisementData["kCBAdvDataLocalName"] as? String
        //data.append(a)
        //self.txtStatus.text = "Connected!"
        
        //////////////peripherals.append(peripheral)
        //tableView.reloadData()
        //viewDidLoad()
    }
    
    func centralManager(central: CBCentralManager,
                        didConnectPeripheral peripheral: CBPeripheral) {
        let date = NSDate();
        let formatter = NSDateFormatter();
        formatter.dateFormat = "yyyy-MM-dd HH:mm:ss ZZZ";
        _ = formatter.stringFromDate(date);
        formatter.timeZone = NSTimeZone(abbreviation: "UTC");
        let utcTimeZoneStr = formatter.stringFromDate(date);
        print("Peripheral connected: " + peripheral.name! + " at " + utcTimeZoneStr)
        //peripheral.delegate = self
        
        //        //---discover the specified service---
        //        var services = [serviceKeyPressesUDID]
        //        peripheral.discoverServices(services)
    }
    
    func centralManager(central: CBCentralManager,
                        didDisconnectPeripheral peripheral: CBPeripheral,
                                                error: NSError?) {
        let date = NSDate();
        let formatter = NSDateFormatter();
        formatter.dateFormat = "yyyy-MM-dd HH:mm:ss ZZZ";
        _ = formatter.stringFromDate(date);
        formatter.timeZone = NSTimeZone(abbreviation: "UTC");
        let utcTimeZoneStr = formatter.stringFromDate(date);
        print("Peripheral disconnected: " + peripheral.name! + "at " + utcTimeZoneStr)
    }
//
//    //UITableView methods
//    func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell
//    {
//        let cell:UITableViewCell = self.tableView.dequeueReusableCellWithIdentifier("cell")! as UITableViewCell
//        
//        let peripheral = peripherals[indexPath.row]
//        let serviceString = peripheral.name == "" ? "No device name" : peripheral.name
//        
//        cell.textLabel?.text = serviceString
//        
//        return cell
//    }
//    
//    func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int
//    {
//        return peripherals.count
//    }

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

