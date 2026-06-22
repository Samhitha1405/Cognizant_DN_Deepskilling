interface Command {
    void execute();
}

class Light {
    private final String location;
    public Light(String location) { this.location = location; }
    public void turnOn()  { System.out.println(location + " light is ON"); }
    public void turnOff() { System.out.println(location + " light is OFF"); }
}

class LightOnCommand implements Command {
    private final Light light;
    public LightOnCommand(Light l)  { this.light = l; }
    public void execute() { light.turnOn(); }
}

class LightOffCommand implements Command {
    private final Light light;
    public LightOffCommand(Light l) { this.light = l; }
    public void execute() { light.turnOff(); }
}

class RemoteControl {
    private Command command;
    public void setCommand(Command c) { this.command = c; }
    public void pressButton()         { command.execute(); }
}

public class CommandTest {
    public static void main(String[] args) {
        Light living  = new Light("Living Room");
        Light bedroom = new Light("Bedroom");

        RemoteControl remote = new RemoteControl();

        remote.setCommand(new LightOnCommand(living));
        remote.pressButton();

        remote.setCommand(new LightOnCommand(bedroom));
        remote.pressButton();

        remote.setCommand(new LightOffCommand(living));
        remote.pressButton();
    }
}