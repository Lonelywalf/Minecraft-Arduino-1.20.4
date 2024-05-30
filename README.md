# ArduinoCraft
A mod that converts a redstone signal into Arduino's ```digitalWrite()```.

Please note that this is my first mod and I'm still learning how to make mods, so there might be some bugs or issues.
## Current feature(s)
- A block called "Arduino Block" which takes redstone signals and converts it to ```digitalWrite()```
- Convert real life signals to redstone with ```digitalRead()```

### Upcoming feature(s)
- (Menu to select the Arduino pin to your desire)
- GUI menu to start or stop the communication
## How to start
Place down the Arduino Block (from the redstone creative menu), 
plug in your Arduino and run the command ```/arduino start <arduino port> <output/input> <baudrate>```.

Use ```input``` if you want to convert redstone to Arduino, and ```output``` if you want to convert Arduino to redstone.

After that you can connect redstone to it and do whatever you want.
## How to stop
Run the command ```/arduino stop```, or if you forgot the port will automatically close when you quit the game.
## How does it work?
The mod uses the [jSerialComm](https://fazecast.github.io/jSerialComm/) library to communicate with the Arduino through the serial port. 

When you use input, the mod will send a signal to the Arduino everytime the block receives redstone power to write high or low to the pin. 

When you use output, the mod will read if the pin is high or low and send a redstone signal to the block.


TODO: make proper README.md
