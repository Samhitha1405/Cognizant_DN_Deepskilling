public class Computer {
    private final String cpu;
    private final String ram;
    private final String storage;
    private final String gpu;
    private final boolean hasBluetooth;
    private final boolean hasWifi;

    private Computer(Builder builder) {
        this.cpu          = builder.cpu;
        this.ram          = builder.ram;
        this.storage      = builder.storage;
        this.gpu          = builder.gpu;
        this.hasBluetooth = builder.hasBluetooth;
        this.hasWifi      = builder.hasWifi;
    }

    public String toString() {
        return "Computer {" +
               "\n  CPU       = " + cpu +
               "\n  RAM       = " + ram +
               "\n  Storage   = " + storage +
               "\n  GPU       = " + (gpu != null ? gpu : "Integrated") +
               "\n  Bluetooth = " + hasBluetooth +
               "\n  WiFi      = " + hasWifi +
               "\n}";
    }

    public static class Builder {
        private final String cpu;
        private final String ram;
        private String storage       = "256GB SSD";
        private String gpu           = null;
        private boolean hasBluetooth = false;
        private boolean hasWifi      = false;

        public Builder(String cpu, String ram) {
            this.cpu = cpu;
            this.ram = ram;
        }

        public Builder storage(String storage)   { this.storage = storage; return this; }
        public Builder gpu(String gpu)           { this.gpu = gpu;         return this; }
        public Builder bluetooth(boolean val)    { this.hasBluetooth = val; return this; }
        public Builder wifi(boolean val)         { this.hasWifi = val;     return this; }

        public Computer build() { return new Computer(this); }
    }
}