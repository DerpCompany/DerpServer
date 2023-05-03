# Derp Server
Derp Company Server Installation

## Requirements

- Linux based system
- Systemd support
- Java 17

**Make sure to follow this step before continuing**

Enable running services without needing to be signed in by running this command.

`loginctl enable-linger $USER`

## Automatic Installation

The automatic installation will execute the steps in the **Manual Installation** phase. If this is your first time installing
this service, it is recommended to read the **Manual Installation**, so you can understand what is going on.

To install the server as a systemd service, run the following:

`./install.sh`

This command will start the server as a systemd service. You can interface with it trough systemctl:

Start server

```systemctl start derpserver.service```

Stop server

```systemctl stop derpserver.service```

Enable the service on boot:

```systemctl enable derpserver.service```

## Manual Installation

### 1. Build the executable.

This will produce a self-contained jar that can be run directly from the command line.

`./gradlew server:bootJar`

The final jar will be found in `server/build/libs/derpserver.jar`.

### 2. Install the executable

Create a location for the executable and the config file.

```
mkdir $HOME/.local/derpserver
```

Copy the executable and create an empty config file.

```
cp server/build/libs/derpserver.jar $HOME/.local/derpserver

touch $HOME/.local/derpserver/application.properties
```

### 3. Configure the service

Edit the `application.properties` file with the properties required by the service.

### 4. Configure the systemd unit

You can use the template for a systemd service. Copy it to your system.

```
mkdir -p $HOME/.config/systemd/user
cp derpserver.service $HOME/.config/systemd/user
```

Now load the service

```systemctl --user daemon-reload```

### 5. Start the service

You can start the service directly from the command line:

```derpserver.jar```

Or use systemd:

```systemctl --user start derpserver.service```

to enable the service on boot you can use:

```systemctl --user enable derpserver.service```
