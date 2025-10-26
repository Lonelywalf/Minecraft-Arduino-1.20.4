#include <Arduino.h>

// ---------------------- change to match your setup --------------------------------
#define BAUDRATE 9600
#define READ_PORT A0
// ----------------------------------------------------------------------------------

  //--------- convert arduino signal to minecraft redstone signal (analog) ------------
  // usage:
  // for (irl) components that have a range of values like a potentiometer
  //
  // how to use:
  // (0. in minecraft: /arduino start <PORT> output analog <BAUDRATE>)
  // 1. read your "analog" value from something like a potentiometer (arduino uno ADC ... 0 - 1023)
  // 2. divide to fit the 17 redstone states (<your max analog value> / 17)
  // 3. add an offset of 3 (because values 0 - 2 cannot be used by serial)
  // 4. send the final value through Serial.println()

  // TL DR:
  // basically just Serial.println() (println !!!) value 3 - 20 (3... lowest redstone signal; 20 ... highest redstone signal)

volatile int potentiometerValue = 0;
volatile int lastPotState = 0;

void setup() {
  Serial.begin(BAUDRATE);
}

void loop() {
  // read the potentiometer value
  potentiometerValue = analogRead(READ_PORT);

  // divide the value so that there are only 17 values
  potentiometerValue /= 60;

  // add 3 to the value because 0-2 are reserved
  potentiometerValue += 3;

  // Only print if the value has changed
  if (potentiometerValue != lastPotState) {
    Serial.println(potentiometerValue);
    lastPotState = potentiometerValue;
  }
}
