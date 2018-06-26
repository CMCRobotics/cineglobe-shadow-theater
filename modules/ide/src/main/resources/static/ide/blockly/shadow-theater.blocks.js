/*
 * Required blocks :
   * Station functions (hard coded)
 */

Blockly.Blocks['station'] = {
  init: function() {
    this.appendDummyInput()
        .setAlign(Blockly.ALIGN_CENTRE)
        .appendField(new Blockly.FieldImage("blockly/assets/images/to_station.png", 60, 60, "*"))
        .appendField(new Blockly.FieldDropdown([["1","station_1"], ["2","station_2"], ["3","station_3"],["4","station_4"], ["Fin","station_5"]]), "STATION");
    this.appendDummyInput()
        .setAlign(Blockly.ALIGN_CENTRE)
  //      .appendField(new Blockly.FieldImage("https://www.gstatic.com/codesite/ph/images/star_on.gif", 30, 30, "Ambiance sonore"))
        .appendField(new Blockly.FieldDropdown([[{"src":"blockly/assets/images/sound_fire.png", "width":55,"height":55,"alt":"Joue l'ambiance sonore FEU"}  ,"sound_fire"]
                                              , [{"src":"blockly/assets/images/sound_metal.png","width":55,"height":55,"alt":"Joue l'ambiance sonore METAL"},"sound_metal"]
                                              , [{"src":"blockly/assets/images/sound_water.png","width":55,"height":55,"alt":"Joue l'ambiance sonore EAU"}  ,"sound_water"]
                                              , [{"src":"blockly/assets/images/sound_wood.png", "width":55,"height":55,"alt":"Joue l'ambiance sonore BOIS"} ,"sound_wood"]
                                              , [{"src":"blockly/assets/images/sound_soil.png", "width":55,"height":55,"alt":"Joue l'ambiance sonore TERRE"},"sound_soil"]])
                                           , "SOUND");
    this.appendStatementInput("EFFECTS")
        .setCheck(null)
        .setAlign(Blockly.ALIGN_CENTRE)
     //   .appendField(new Blockly.FieldImage("https://www.gstatic.com/codesite/ph/images/star_on.gif", 30, 30, "*"))
        ;
    this.setColour(0);
 this.setTooltip("");
 this.setHelpUrl("");
  }
};
 
//Blockly.Blocks['sound_fire'] = {
//  init: function() {
//    this.appendDummyInput()
//        .appendField(new Blockly.FieldImage("blockly/assets/images/sound_fire.png", 80, 80, "Play the 'fire' sound"));
//    this.setPreviousStatement(true, null);
//    this.setNextStatement(true, null);
//    this.setColour(90);
// this.setTooltip("Joue le son 'Feu'");
// this.setHelpUrl("");
//  }
//};
//
//Blockly.Blocks['sound_metal'] = {
//  init: function() {
//    this.appendDummyInput()
//        .appendField(new Blockly.FieldImage("blockly/assets/images/sound_metal.png", 80, 80, "Play the 'metal' sound"));
//    this.setPreviousStatement(true, null);
//    this.setNextStatement(true, null);
//    this.setColour(90);
// this.setTooltip("Joue le son 'Metal'");
// this.setHelpUrl("");
//  }
//};
//Blockly.Blocks['sound_water'] = {
//  init: function() {
//    this.appendDummyInput()
//        .appendField(new Blockly.FieldImage("blockly/assets/images/sound_water.png", 80, 80, "Play the 'water' sound"));
//    this.setPreviousStatement(true, null);
//    this.setNextStatement(true, null);
//    this.setColour(90);
// this.setTooltip("Joue le son 'Eau'");
// this.setHelpUrl("");
//  }
//};
//Blockly.Blocks['sound_wood'] = {
//  init: function() {
//    this.appendDummyInput()
//        .appendField(new Blockly.FieldImage("blockly/assets/images/sound_wood.png", 80, 80, "Play the 'wood' sound"));
//    this.setPreviousStatement(true, null);
//    this.setNextStatement(true, null);
//    this.setColour(90);
// this.setTooltip("Joue le son 'Bois'");
// this.setHelpUrl("");
//  }
//};
//Blockly.Blocks['sound_soil'] = {
//  init: function() {
//    this.appendDummyInput()
//        .appendField(new Blockly.FieldImage("blockly/assets/images/sound_soil.png", 80, 80, "Play the 'soil' sound"));
//    this.setPreviousStatement(true, null);
//    this.setNextStatement(true, null);
//    this.setColour(90);
// this.setTooltip("Joue le son 'Terre'");
// this.setHelpUrl("");
//  }
//};

Blockly.Blocks['motor_forward'] = {
  init: function() {
    this.appendDummyInput()
        .appendField(new Blockly.FieldImage("blockly/assets/images/motor_forward.png", 70,70, "Move forward"));
    this.setPreviousStatement(true, null);
    this.setNextStatement(true, null);
    this.setColour(0);
 this.setTooltip("Avance le robot");
 this.setHelpUrl("");
  }
};

Blockly.Blocks['motor_backward'] = {
  init: function() {
    this.appendDummyInput()
        .appendField(new Blockly.FieldImage("blockly/assets/images/motor_backward.png", 70,70, "Move backward"));
    this.setPreviousStatement(true, null);
    this.setNextStatement(true, null);
    this.setColour(0);
 this.setTooltip("Recule le robot");
 this.setHelpUrl("");
  }
};
 
Blockly.Blocks['motor_full_turn'] = {
  init: function() {
    this.appendDummyInput()
        .appendField(new Blockly.FieldImage("blockly/assets/images/motor_full_turn.png", 70,70, "Perform a full turn"));
    this.setPreviousStatement(true, null);
    this.setNextStatement(true, null);
    this.setColour(0);
 this.setTooltip("Fait pivoter le robot sur lui-même");
 this.setHelpUrl("");
  }
};

Blockly.Blocks['light_on'] = {
  init: function() {
    this.appendDummyInput()
        .appendField(new Blockly.FieldImage("blockly/assets/images/light_on.png", 70,70, "Turn on the spotlight"));
    this.setPreviousStatement(true, null);
    this.setNextStatement(true, null);
    this.setColour(210);
 this.setTooltip("Allume le spot camera");
 this.setHelpUrl("");
  }
};

Blockly.Blocks['light_off'] = {
  init: function() {
    this.appendDummyInput()
        .appendField(new Blockly.FieldImage("blockly/assets/images/light_off.png", 70,70, "Turn off the spotlight"));
    this.setPreviousStatement(true, null);
    this.setNextStatement(true, null);
    this.setColour(210);
 this.setTooltip("Eteint le spot caméra");
 this.setHelpUrl("");
  }
};

Blockly.Blocks['light_thunder'] = {
  init: function() {
    this.appendDummyInput()
        .appendField(new Blockly.FieldImage("blockly/assets/images/light_thunder.png", 70,70, "Start a thunder light effect"));
    this.setPreviousStatement(true, null);
    this.setNextStatement(true, null);
    this.setColour(210);
 this.setTooltip("Simule un orage avec le spot caméra");
 this.setHelpUrl("");
  }
};
Blockly.Blocks['light_wave'] = {
  init: function() {
    this.appendDummyInput()
        .appendField(new Blockly.FieldImage("blockly/assets/images/light_wave.png", 70,70, "Start a breathing light effect"));
    this.setPreviousStatement(true, null);
    this.setNextStatement(true, null);
    this.setColour(210);
 this.setTooltip("Simule une respiration avec le spot caméra");
 this.setHelpUrl("");
  }
};

Blockly.Blocks['camera_pan_left'] = {
  init: function() {
    this.appendDummyInput()
        .appendField(new Blockly.FieldImage("blockly/assets/images/camera_pan_left.png", 70,70, "Camera Pan Left"));
    this.setPreviousStatement(true, null);
    this.setNextStatement(true, null);
    this.setColour(165);
 this.setTooltip("Pivote la camera sur la gauche");
 this.setHelpUrl("");
  }
};
Blockly.Blocks['camera_pan_right'] = {
  init: function() {
    this.appendDummyInput()
        .appendField(new Blockly.FieldImage("blockly/assets/images/camera_pan_right.png", 70,70, "Camera Pan Right"));
    this.setPreviousStatement(true, null);
    this.setNextStatement(true, null);
    this.setColour(165);
 this.setTooltip("Pivote la camera sur la droite");
 this.setHelpUrl("");
  }
};

Blockly.Blocks['camera_tilt_up'] = {
  init: function() {
    this.appendDummyInput()
        .appendField(new Blockly.FieldImage("blockly/assets/images/camera_tilt_up.png", 70,70, "Camera Tilt Up"));
    this.setPreviousStatement(true, null);
    this.setNextStatement(true, null);
    this.setColour(165);
 this.setTooltip("Pivote la camera vers le haut");
 this.setHelpUrl("");
  }
};

Blockly.Blocks['camera_tilt_down'] = {
  init: function() {
    this.appendDummyInput()
        .appendField(new Blockly.FieldImage("blockly/assets/images/camera_tilt_down.png", 70,70, "Camera Tilt Down"));
    this.setPreviousStatement(true, null);
    this.setNextStatement(true, null);
    this.setColour(165);
 this.setTooltip("Pivote la camera vers le bas");
 this.setHelpUrl("");
  }
};



