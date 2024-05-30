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
  // read the button signal
  buttonState = digitalRead(inputPin);

  // only send signal when the button state changes
  if (buttonState != lastButtonState) {
    // if the state has changed, print to the serial monitor
    if (buttonState == LOW) {
    
      Serial.print("3");

    } else if (buttonState == HIGH) {

      Serial.print("4");  // send a different signal when the button is not held down
    
    }

    // save the current state as the last state, for next time through the loop
    lastButtonState = buttonState;
  }
  //-----------------------------------------------------------------------------------




  //---------------- convert minecraft redstone signal to arduino signal --------------
  // 
  if (Serial.available() > 0) {

    // read the signal from minecraft mod
    input = Serial.read();
    

    // writes to the pin

    // this gets run when redstone signal is received,
    // so change what will happen when redstone signal is received here
    if (input == 1) {

      digitalWrite(outputPin, HIGH);

    }

    // this gets run when NO redstone signal is received,
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
