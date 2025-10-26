# ArduinoCraft
A Minecraft Fabric mod that allows redstone signals to interact with an Arduino and vice versa.

Please note that this is my first mod, and I'm still learning how to make mods, so there might be some bugs or issues. If you have any issues or questions, DM me on Discord **@jamicah**.

## Features
- An "Arduino Block" which takes redstone signals and converts it to `digitalWrite()`
- Convert real life signals to redstone with ```digitalRead()```

### TODO:
- A GUI menu for closing/opening communication with Arduino

## How to use

### Upload a sketch
You can choose an [example sketch](arduino/example) and upload it to your Arduino using the Arduino IDE (or whatever you use to upload ig). I'd recommend starting with the [digitalRead/digitalWrite example](arduino/example/digital_read_receive_example) to see how the communication works.


### Start
In Minecraft, place down the Arduino Block (from the redstone creative menu), 
plug in your Arduino and run the command `/arduino start <arduino port> <output/input> <baudrate>`.

Use `input` if you want to convert redstone signals to Arduino, and `output` if you want to convert Arduino to redstone.


### Stop
You may use the command `/arduino stop` to close the port. The mod will also stop when you quit the game

## How does it work?
ArduinoCraft uses the [jSerialComm](https://fazecast.github.io/jSerialComm/) library to communicate with the Arduino through the serial port. 

When you use input, the mod will send a signal to the Arduino everytime the block receives redstone power to write high or low to the pin. 

When you use output, the mod will check the pin and send a matching redstone signal to the block.

It is highly recommend that you look into the [template sketch](arduino/example.ino) to see how the communication works.
