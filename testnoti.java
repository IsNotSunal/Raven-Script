List<modInfo> mods = Arrays.asList(new modInfo("Speed" , false));
List<notiInfo> queue = new ArrayList<>();

double animated;

void onRenderTick(float partialTicks) {
    if (!client.getScreen().isEmpty()) {
        return;
    }
    animated = lerp(animated, 50, 0.05);
    client.render.text("TESTY",(float) animated, 15, 1, new Color(255 , 255 , 255).getRGB(), true);
}

void onEnable(){
    animated = 0.0;
}

double fps_multiplier(){
    return 60.0 / client.getFPS();
}

double exponential_anim(double current , double intended , double speed){
    double returning = current;

    if (current < intended) {
        returning = current + (current - intended) * -speed * fps_multiplier();
    }else if (current > intended) {
        returning = current - (current - intended) * speed * fps_multiplier();
    }else{
        returning = current;
    }

    return returning;
}

double lerp(double min , double max , double fraction){
    return (max- min) * fraction + min;
}

static class modInfo {
    String modName;
    boolean state;

    modInfo(String modName , boolean state){
        this.modName = modName;
        this.state = state;
    }

    public void setModName(String modName) {
        this.modName = modName;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public String getModName() {
        return modName;
    }

    public boolean getState(){
        return state;
    }
}

static class notiInfo {
    String name;
    int tick;
    boolean state;
    double current_pos;
    double target_pos;
    double backen;
    double current_y_pos;
    double target_y_pos;
    boolean set_first_y_pos;

    notiInfo(String name , int tick , boolean state , double current_pos , double target_pos , double backen , double current_y_pos , double target_y_pos , boolean set_first_y_pos){
        this.name = name;
        this.tick = tick;
        this.state = state;
        this.current_pos = current_pos;
        this.target_pos = target_pos;
        this.backen = backen;
        this.current_y_pos = current_y_pos;
        this.target_y_pos = target_y_pos;
        this.set_first_y_pos = set_first_y_pos;
    }

    public String getName() {
        return name;
    }

    public int getTick() {
        return tick;
    }

    public boolean getState(){
        return state;
    }

    public double getCurrent_pos() {
        return current_pos;
    }

    public double getTarget_pos() {
        return target_pos;
    }

    public double getBacken() {
        return backen;
    }

    public double getCurrent_y_pos() {
        return current_y_pos;
    }

    public double getTarget_y_pos() {
        return target_y_pos;
    }

    public boolean getSetFirstYPos(){
        return set_first_y_pos;
    }
}

