#include <Arduino.h>

byte input;
int buttonState = 0;
int lastButtonState = 0; 

// ---------------------- change to match your setup --------------------------------
const int inputPin = 3;
const int outputPin = 2;
const int baudrate = 115200; // values I know: 115200 for ESP32, 9600 for Arduino uno
// ----------------------------------------------------------------------------------

void setup() {

  pinMode(outputPin, OUTPUT);
  pinMode(inputPin, INPUT_PULLUP);
  Serial.begin(baudrate);
  digitalWrite(outputPin, LOW);

}

void loop() {

  //---------------- convert arduino signal to minecraft redstone signal --------------
  // how it works:
  // when the button (irl) is pressed down, the arduino will send '3' through Serial
  // for the mod to read and change the outputted redstone signal to ON.
  // '4' will be sent when the button is released to change to OFF.

  // read the button signal
  buttonState = digitalRead(inputPin);

  // only send signal when the button state changes
  if (buttonState != lastButtonState) {

    // this runs when the button is pressed down,
    // so change when the redstone signal changes to ON
    if (buttonState == LOW) {
    
      Serial.print("3");

    }

    // this runs when the button is released,
    // so change when the redstone signal changes to OFF
    else if (buttonState == HIGH) {   // button released

      Serial.print("4");  // send a different signal when the button is not held down
    
    }

    // save the current state as the last state, for next time through the loop
    lastButtonState = buttonState;
  }
  //-----------------------------------------------------------------------------------




  //---------------- convert minecraft redstone signal to arduino signal --------------
  // how it works:
  // when the block receives a redstone signal, it will send '1'
  // to the arduino through Serial and when
  // the redstone signal changes from on to off it will send '0'
  if (Serial.available() > 0) {

    // read the signal from minecraft mod
    input = Serial.read();
    

    // writing to the pin

    // this gets run when redstone signal is received,
    // so change what will happen when redstone signal is received here
    if (input == 1) {

      digitalWrite(outputPin, HIGH);

    }

    // this gets run when the redstone signal changes
    // from on to off,
    // so change what will happen when NO redstone signal is received here 
    else if (input == 0) {

      digitalWrite(outputPin, LOW);

    }
  //-----------------------------------------------------------------------------------


    /* debug
    if (input != -1) {
      Serial.println(input);
    }
    */
  
  }

  
}
