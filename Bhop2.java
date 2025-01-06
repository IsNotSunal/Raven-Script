String[] modes = {"LowHop" , "FastFall" , "Normal"};
int airticks;

void onLoad(){
    modules.registerSlider("Mode" , "" , 0 , modes);
    modules.registerButton("Glide Strafe", false);
    modules.registerButton("Extra Strafe", false);
    modules.registerButton("Speed Boost", false);
    modules.registerButton("Stop on Scaffold" , false);
    modules.registerButton("Disable on Flag" , false);
    modules.registerButton("Show Debug" , false);
}

void onPostPlayerInput() {
    if (modules.isEnabled("Scaffold") && modules.getButton(scriptName , "Stop on Scaffold")) return;

    client.setJump(false);
}

void onPreMotion(PlayerState state){
    Entity player = client.getPlayer();
    if (player.onGround()){
        printDebug(airticks);
    }
    airticks = player.onGround() ? 0 : airticks + 1;

    if (modules.isEnabled("Scaffold") && modules.getButton(scriptName , "Stop on Scaffold")) {
        return;
    }

    if(player.isInWater() || player.isInLava() || player.isOnLadder()){
        return;
    }

    if (player.onGround() && client.isMoving()){
        client.jump();
        client.setSpeed(getSpeed());
    }

    if (airticks == 1){
        if (modules.getButton(scriptName, "Speed Boost") && speedLvl() > 1){
            Vec3 motion = client.getMotion();
            client.setMotion(motion.x * 1.05, motion.y, motion.z * 1.05);
        }
        
        if (modules.getButton(scriptName, "Extra Strafe")){
            client.setSpeed(player.getSpeed());
        }
    }

    switch (getMode()) {
        case "LowHop":
            if (airticks == 1){
                Vec3 motion = client.getMotion();
                client.setMotion(motion.x, motion.y + 0.057, motion.z);
            }
            if (airticks == 3){
                Vec3 motion = client.getMotion();
                client.setMotion(motion.x, motion.y - 0.1309, motion.z);
            }
            if (airticks == 4){
                Vec3 motion = client.getMotion();
                client.setMotion(motion.x, motion.y - 0.2, motion.z);
            }

            if (airticks == 6 && modules.getButton(scriptName, "Glide Strafe") && player.distanceToGround() < 0.75){
                Vec3 motion = client.getMotion();
                client.setMotion(motion.x, motion.y + 0.075, motion.z);
                client.setSpeed(player.getSpeed());
                printDebug("LH Glide Strafe");
            }
            break;
        case "FastFall":
            if (airticks == 5){
                Vec3 motion = client.getMotion();
                double motiony = motion.y;
                motiony = (motiony - 0.07) * 0.98;
                motiony = (motiony - 0.07) * 0.98;
                client.setMotion(motion.x, motiony, motion.z);
            }

            if (airticks == 8 && modules.getButton(scriptName, "Glide Strafe")){
                Vec3 motion = client.getMotion();
                client.setMotion(motion.x, motion.y + 0.075, motion.z);
                client.setSpeed(player.getSpeed());
                printDebug("FF Glide Strafe");
            }
            break;
        case "Normal":
            if (airticks == 9 && modules.getButton(scriptName, "Glide Strafe")){
                Vec3 motion = client.getMotion();
                client.setMotion(motion.x, motion.y + 0.075, motion.z);
                client.setSpeed(player.getSpeed());
                printDebug("N Glide Strafe");
            }
            break;
    }
}

boolean onPacketReceived(SPacket packet) {
    if (packet instanceof S08 && modules.getButton(scriptName, "Disable on Flag")) {
        modules.disable(scriptName);
    }
    return true;
}

void onEnable(){
    airticks = 0;
}

double getSpeed() {
    if (speedLvl() == 0) return 0.48;
    if (speedLvl() == 1) return 0.51;
    if (speedLvl() == 2) return 0.59;
    if (speedLvl() == 3) return 0.69;
    if (speedLvl() == 4) return 0.78;

    return 0.48; //default
}

int speedLvl() {
    for (Object[] effect : client.getPlayer().getPotionEffects()) {
        String name = (String) effect[1];
        int amplifier = (int) effect[2];
        if (name.equals("potion.moveSpeed")) {
            return amplifier + 1;
        }
        return 0;
    }
    return 0;
}

String getMode() {
    return modes[ (int) modules.getSlider(scriptName, "Mode")];
}

void printDebug(Object message) {
    if (modules.getButton(scriptName, "Show Debug")) {
        client.print(message);
    }
}