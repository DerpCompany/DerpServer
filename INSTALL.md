# Derp Server
Derp Company Server Installation

## Requirements

- Linux based system
- Systemd support
- Java 17

## Manual Installation

### 1. Build the executable.

This will produce a self-contained jar that can be run directly from the command line.

`./gradlew server:bootJar`

The final jar will be found in `server/build/libs/derpserver.jar`.

### 2. Install the executable

Create a location for the executable and the config file.

```
sudo mkdir /opt/derpserver/
chown $USER:$GROUP /opt/derpserver/
```

Copy the executable and create an empty config file.

```
cp server/build/libs/derpserver.jar /opt/derpserver/derpserver.jar

touch /opt/derpserver/application.properties
```

### 3. Configure the service

Edit the `application.properties` file with the properties required by the service.

### 4. Configure the systemd unit

You can use the template for a systemd service. Copy it to your system.

```
sudo cp server/install/derpserver.service /etc/systemd/system/derpserver.service
```

You should edit the file to make sure it points to the right folder and to set the correct user to use.

Now load the service

```systemctl daemon-reload```

### 5. Start the service

You can start the service directly from the command line:

```/opt/derpserver/derpserver.jar```

Or use systemd:

```systemctl start derpserver.service```

to enable the service on boot you can use:

```systemctl enable derpserver.service```
