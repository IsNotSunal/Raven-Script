String[] modes = {"Flat" , "High"};

boolean start , done;
int ticks;
void onLoad(){
    modules.registerSlider("Mode" , "" , 0 , modes);
    modules.registerButton("Strafe", false);
}

void onPreUpdate(){
    Entity player = client.getPlayer();
    Vec3 motion = client.getMotion();
    if (player.getHurtTime() > 3){
        start = true;
    }

    if (start){
        ticks++;
    }

    switch (getMode()){
        case "Flat":
            if (ticks != 0 && ticks < 29){
                client.setMotion(motion.x, 0.09, motion.z);
            }

            if (ticks >= 30){
                done = true;
            }
            break;

        case "High":
            if (ticks != 0 && ticks < 25){
                client.setMotion(motion.x, 0.35 , motion.z);
            }

            if (ticks >= 25){
                done = true;
            }
            break;
    }

    if (player.getHurtTime() == 0 && done){
        modules.disable(scriptName);
        start = false;
        done = false;
        ticks = 0;
    }
}

void onPreMotion(PlayerState state){
    Entity player = client.getPlayer();
    Vec3 motion = client.getMotion();
    
    if (modules.getButton(scriptName, "Strafe") && !done && start && ticks != 0){
        client.setSpeed(player.getSpeed());
    }
}

boolean onPacketReceived(SPacket packet) {
    if (packet instanceof S08) {
        start = false;
        done = false;
        ticks = 0;
        modules.disable(scriptName);
    }
    return true;
}

void onDisable(){
    start = false;
    done = false;
    ticks = 0;
    modules.disable("Long Jump");
}

void onEnable(){
    start = false;
    done = false;
    ticks = 0;
    modules.enable("Long Jump");
}

String getMode() {
    return modes[ (int) modules.getSlider(scriptName, "Mode")];
}