# ArduinoCraft
A Minecraft Fabric mod that allows redstone signals to interact with an Arduino and vice versa.

Please note that this was my first mod, and I'm still learning how to make mods, so there might be some bugs or issues. If you have any issues or questions, DM me on Discord **@jamicah**.

This mod is not only limited to Arduino. It *should* work with any microcontroller that supports Serial UART communication over USB.

NOTE: This mod requires a bit of knowledge about Arduino and Serial communication. If you are new to Arduino, I recommend checking out tutorials on Arduino and Serial communication first.

## Features
- Convert redstone signals to Arduino digital signals
- Convert Arduino digital/analog signals to redstone signals

### TODO:
- A GUI menu for closing/opening communication with Arduino

## How to use

### Upload a sketch
You can choose an [example sketch](arduino/example) and upload it to your Arduino using the Arduino IDE (or whatever you use to upload ig). I'd recommend starting with the [digitalRead/digitalWrite example](arduino/example/digital_read_receive_example) to see how the communication works. Images for wiring are also included in the example folders.


### Start
In Minecraft, place down the Arduino Block (from the redstone creative menu), 
plug in your Arduino and run the command
- `/arduino start <arduino port> output <baudrate>`. Redstone to Arduino
- `/arduino start <arduino port> input <analog/digital> <baudrate>`. Arduino to Redstone

NOTE: If you're not on Windows, you need to use quotes around the port name. For example, on Linux or MacOS, you might use `/arduino start "/dev/ttyACM0" output 9600`.

### Stop
You may use the command `/arduino stop` to close the port. The mod will also stop when you quit the game.

## How does it work?
ArduinoCraft communicates with the Arduino using Serial communication over USB.

When you use input, the mod will send a signal to the Arduino everytime the block receives redstone power to write high or low to the pin. 

When you use output, the mod will check the pin and send a matching redstone signal to the block.

It is highly recommend that you look into the [example sketches](arduino/example) to see how the communication works.
