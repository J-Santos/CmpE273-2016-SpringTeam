#include <SimbleeBLE.h>


// pin 3 on the RGB sheild is the green led
int led1 = 3;

// pin 2 on the RGB shield is the red led
int led2 = 2;

void setup() {
  // put your setup code here, to run once:

  // configure leds
  pinMode(led1, OUTPUT);
  pinMode(led2, OUTPUT);

  // turn on led1 to indicate we are waiting for a connection
  digitalWrite(led1, HIGH);

  // drop the transmission power to move the zones closer to
  // the Simblee (which also reduces the interference from
  // other wireless devices and reflection)
  // (all zones are within approxiately 10 feet of the Simblee)
  SimbleeBLE.txPowerLevel = -20;
  
  //SimbleeBLE.deviceName = “SpringTeamSimblee”; 
  SimbleeBLE.advertisementData = "AcademicSystem";
  SimbleeBLE.deviceName = "WRL-13632";
  SimbleeBLE.begin();

}

void loop() {
  // put your main code here, to run repeatedly:
  // switch to lower power mode
  Simblee_ULPDelay(INFINITE);
  SimbleeBLE.send(1);

}

void SimbleeBLE_onConnect()
{
  // turn off led1 when we have a connection
  digitalWrite(led1, LOW);
}

void SimbleeBLE_onDisconnect()
{
  // don't leave the leds on if they disconnect
  digitalWrite(led1, LOW);
  digitalWrite(led2, LOW);
}

// returns the dBm signal strength indicated by the receiver
// received signal strength indication (-0dBm to -127dBm)
void SimbleeBLE_onRSSI(int rssi)
{
  // turn off both leds if the signal strength is out of range
  if (rssi <= -75)
  {
    digitalWrite(led1, LOW);
    digitalWrite(led2, LOW);
  }
  // in range, but signal strength is weak
  else if (rssi <= -65)
  {
    digitalWrite(led1, HIGH);
    digitalWrite(led2, LOW);
  }
  // in range, signal strength is stronger
  else if (rssi <= -55)
  {
    digitalWrite(led1, HIGH);
    digitalWrite(led2, HIGH);
  }
  // in range, signal strength is strong
  else
  {
    digitalWrite(led1, LOW);
    digitalWrite(led2, HIGH);
  }
}
