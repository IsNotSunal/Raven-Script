int airticks , flag;
boolean jump , disabled;
float yawmodfier;

void onLoad(){
    modules.registerButton("Lobby Check", true);
}

void onEnable(){
    airticks = 0;
    flag = 0;
    jump = false;
    disabled = false;
    yawmodfier = 1;
}


void onPreMotion(PlayerState state){
    Entity player = client.getPlayer();
    airticks = player.onGround() ? 0 : airticks + 1;
    if(isLobby() && modules.getButton(scriptName , "Lobby Check")){
        return;
    }   
    
    if(player.onGround() && jump){
        jump = false;
        disabled = true;
        client.jump();
    }else if (disabled && airticks > 9){
        if (airticks % 2 == 0){
            Random rand = new Random();
            state.z += 0.095 + rand.nextFloat() / 10;
            state.yaw += yawmodfier;
        }
    }
}

boolean onPacketSent(CPacket packet){
    if (packet instanceof C01){
        C01 chatMessage = (C01) packet;
        client.print(chatMessage.message);
        if (chatMessage.message.equals("force")){
            jump = true;
            disabled = false;
            flag = 0;
            airticks = 0;
            client.print("Start Disable Motion Check...");
            return false;
        }
    }

    return true;
}

void onPostPlayerInput(){
    if(isLobby() && modules.getButton(scriptName , "Lobby Check")){
        return;
    }

    if (disabled && airticks > 9) {
        client.setForward(0);
        client.setStrafe(0);
        client.setMotion(0, 0, 0);
    }
}

boolean onPacketReceived(SPacket packet) {
    if(isLobby() && modules.getButton(scriptName , "Lobby Check")){
        return true;
    }

    if (packet instanceof S08){
        if (disabled){
            flag++;
            yawmodfier = -yawmodfier;
            client.print("flag");
            if (flag > 20) {
                disabled = false;
                flag = 0;
                client.print("Disable Motion Check Completed!");
            }
        }
    }
    return true;
}

void onWorldJoin(Entity entity){
    if (entity == client.getPlayer()) {
        jump = true;
        disabled = false;
        flag = 0;
        airticks = 0;
        client.print("Start Disable Motion Check...");
    }
}

boolean isLobby(){
    if(findNetherStar() && findCompass()) return true;
    return false;
}

boolean findNetherStar(){
    for(int i = 0; i < 9; i++){
        ItemStack item = inventory.getStackInSlot(i);
        if(item == null) continue;

        if(item.name.equals("nether_star")){
            return true;
        }
    }

    return false;
}

boolean findCompass(){
    for(int i = 0; i < 9; i++){
        ItemStack item = inventory.getStackInSlot(i);
        if(item == null) continue;

        if(item.name.equals("compass")){
            return true;
        }
    }

    return false;
}