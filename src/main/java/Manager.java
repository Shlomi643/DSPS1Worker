public class Manager {

    private static Manager manager;
    private boolean active;

    private Manager() {
        this.active = false;
    }

    public static Manager getInstance() {
        if (manager == null)
            manager = new Manager();
        return manager;
    }

    public void activate(){
        this.active = true;
    }

    public boolean isActive(){
        return this.active;
    }
}
