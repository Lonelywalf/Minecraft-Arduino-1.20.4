#include <Arduino.h>
/*
  Example sketch for sending/reading digital signals.
  
  This sketch explains how to correctly use the Minecraft Arduino Mod.
  You can also use this sketch as a base for your project.
*/

#define SEND_HIGH "3" // value to send a ON output signal
#define SEND_LOW "4" // value to send a OFF output signal
#define RECEIVE_HIGH 1 // value received if an ON redstone signal is received
#define RECEIVE_LOW 0 // value received if an OFF redstone signal is received

// ---------------------- change to match your setup --------------------------------
#define INPUT_PIN 3
#define OUTPUT_PIN 13
#define BAUDRATE 9600 // common values: 115200 for ESP32, 9600 for Arduino uno / nano
// ----------------------------------------------------------------------------------

volatile byte input;
volatile int buttonState = 0;
volatile int lastButtonState = 0;

void setup() {
  pinMode(OUTPUT_PIN, OUTPUT);
  pinMode(INPUT_PIN, INPUT_PULLUP);
  Serial.begin(BAUDRATE);
  digitalWrite(OUTPUT_PIN, LOW);
}

void loop() {

  //-------- convert arduino signal to minecraft redstone signal -----------
  // usage:
  // for (irl) components that only have an on/off state like a button or a switch
  //
  // how to use:
  // (0. /arduino start <PORT> output digital <BAUDRATE>)
  // 1. read your button value (HIGH, LOW)
  // 2. send "3" to toggle redstone ON, send "4" to toggle redstone OFF through Serial.print()

  // additional info: you don't need to constantly send these values, the redstone signal toggles.


  // read the button signal
  buttonState = digitalRead(INPUT_PIN);

  // only send signal when the button state changes
  if (buttonState != lastButtonState) {

    // this runs when the button is pressed down,
    // so change when the redstone signal changes to ON
    if (buttonState == LOW) {
    
      Serial.print(SEND_HIGH);

    }

    // this runs when the button is released,
    // so change when the redstone signal changes to OFF
    else if (buttonState == HIGH) {   // button released

      Serial.print(SEND_LOW);  // send a different signal when the button is not held down
    
    }

    // save the current state as the last state, for next time through the loop
    lastButtonState = buttonState;
  }
  //-----------------------------------------------------------------------------------



  //---------------- convert minecraft redstone signal to arduino signal --------------
  // how to use:
  // (0. /arduino start <PORT> input <BAUDRATE>)
  // 1. read the redstone signal through Serial.read()
  // 2. Serial.read() = 1 if redstone is ON; Serial.read() = 0 if redstone if OFF

  
  if (Serial.available() > 0) {

    // read the signal from minecraft mod
    input = Serial.read();
    

    // writing to the pin

    // this gets run when redstone signal is received,
    // so change what will happen when redstone signal is received here
    if (input == RECEIVE_HIGH) {

      digitalWrite(OUTPUT_PIN, HIGH);

    }

    // this gets run when the redstone signal changes
    // from on to off,
    // so change what will happen when NO redstone signal is received here 
    else if (input == RECEIVE_LOW) {

      digitalWrite(OUTPUT_PIN, LOW);

    }
  //-----------------------------------------------------------------------------------


    /* debug
    if (input != -1) {
      Serial.println(input);
    }
    */
  
  }
}