#include <Arduino.h>
#define SEND_HIGH "3" // value to send a ON output signal
#define SEND_LOW "4" // value to send a OFF output signal
#define RECEIVE_HIGH 1 // value received if an ON redstone signal is received
#define RECEIVE_LOW 0 // value received if an OFF redstone signal is received

byte input;
int buttonState = 0;
int lastButtonState = 0;
int potentiometerValue = 0;
int lastPotState = 0;

// ---------------------- change to match your setup --------------------------------
const int inputPin = 3;
const int outputPin = 2;
const int baudrate = 9600; // values I know: 115200 for ESP32, 9600 for Arduino uno
// ----------------------------------------------------------------------------------

void setup() {

  pinMode(outputPin, OUTPUT);
  pinMode(inputPin, INPUT_PULLUP);
  Serial.begin(baudrate);
  digitalWrite(outputPin, LOW);

}

void loop() {

  //-------- convert arduino signal to minecraft redstone signal (digital) -----------
  // usage:
  // for (irl) components that only have an on/off state like a button or a switch
  //
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



  // do not use... it is not implemented in the mod (yet)...
  //--------- convert arduino signal to minecraft redstone signal (analog) ------------
  // usage:
  // for (irl) components that have a range of values like a potentiometer
  //
  // how it works:
  // the arduino reads the value from the potentiometer, divides it so that
  // there are only 17 values and adds 3, since the values 0 - 2 are reserved
  // and cannot be used. The arduino will send the value to the mod through Serial
  // and the mod will change the outputted redstone signal to the value received.

  /* ! uncomment to use and comment everything else !
  // read the potentiometer value
  potentiometerValue = analogRead(A0);

  // divide the value so that there are only 17 values
  potentiometerValue /= 60;

  // add 3 to the value because 0-2 are reserved
  potentiometerValue += 3;

  // Only print if the value has changed
  if (potentiometerValue != lastPotState) {
    Serial.println(potentiometerValue);
    lastPotState = potentiometerValue;
  }
  //-----------------------------------------------------------------------------------
  ! uncomment to use ! */




  //---------------- convert minecraft redstone signal to arduino signal --------------
  // how it works:
  // when the block receives a redstone signal, it will send '1' (RECEIVE_HIGH)
  // to the arduino through Serial and when
  // the redstone signal changes from on to off it will send '0' (RECEIVE_LOW)
  if (Serial.available() > 0) {

    // read the signal from minecraft mod
    input = Serial.read();
    

    // writing to the pin

    // this gets run when redstone signal is received,
    // so change what will happen when redstone signal is received here
    if (input == RECEIVE_HIGH) {

      digitalWrite(outputPin, HIGH);

    }

    // this gets run when the redstone signal changes
    // from on to off,
    // so change what will happen when NO redstone signal is received here 
    else if (input == RECEIVE_LOW) {

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
