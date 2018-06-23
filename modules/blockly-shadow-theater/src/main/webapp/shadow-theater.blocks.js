/*
 * Required blocks :
   * Station functions (hard coded)
 */

Blockly.Blocks['sound_fire'] = {
  init: function() {
    this.appendDummyInput()
        .appendField(new Blockly.FieldImage("assets/images/sound_fire.png", 80, 80, "Play the 'fire' sound"));
    this.setPreviousStatement(true, null);
    this.setNextStatement(true, null);
    this.setColour(90);
 this.setTooltip("Joue le son 'Feu'");
 this.setHelpUrl("");
  }
};

Blockly.Blocks['sound_metal'] = {
  init: function() {
    this.appendDummyInput()
        .appendField(new Blockly.FieldImage("assets/images/sound_metal.png", 80, 80, "Play the 'metal' sound"));
    this.setPreviousStatement(true, null);
    this.setNextStatement(true, null);
    this.setColour(90);
 this.setTooltip("Joue le son 'Metal'");
 this.setHelpUrl("");
  }
};
Blockly.Blocks['sound_water'] = {
  init: function() {
    this.appendDummyInput()
        .appendField(new Blockly.FieldImage("assets/images/sound_water.png", 80, 80, "Play the 'water' sound"));
    this.setPreviousStatement(true, null);
    this.setNextStatement(true, null);
    this.setColour(90);
 this.setTooltip("Joue le son 'Eau'");
 this.setHelpUrl("");
  }
};
Blockly.Blocks['sound_wood'] = {
  init: function() {
    this.appendDummyInput()
        .appendField(new Blockly.FieldImage("assets/images/sound_wood.png", 80, 80, "Play the 'wood' sound"));
    this.setPreviousStatement(true, null);
    this.setNextStatement(true, null);
    this.setColour(90);
 this.setTooltip("Joue le son 'Bois'");
 this.setHelpUrl("");
  }
};
Blockly.Blocks['sound_soil'] = {
  init: function() {
    this.appendDummyInput()
        .appendField(new Blockly.FieldImage("assets/images/sound_soil.png", 80, 80, "Play the 'soil' sound"));
    this.setPreviousStatement(true, null);
    this.setNextStatement(true, null);
    this.setColour(90);
 this.setTooltip("Joue le son 'Terre'");
 this.setHelpUrl("");
  }
};

Blockly.Blocks['motor_forward'] = {
  init: function() {
    this.appendDummyInput()
        .appendField(new Blockly.FieldImage("assets/images/motor_forward.png", 64, 64, "Move forward"));
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
        .appendField(new Blockly.FieldImage("assets/images/motor_backward.png", 64, 64, "Move backward"));
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
        .appendField(new Blockly.FieldImage("assets/images/motor_full_turn.png", 64, 64, "Perform a full turn"));
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
        .appendField(new Blockly.FieldImage("assets/images/light_on.png", 64, 64, "Turn on the spotlight"));
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
        .appendField(new Blockly.FieldImage("assets/images/light_on.png", 64, 64, "Turn off the spotlight"));
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
        .appendField(new Blockly.FieldImage("assets/images/light_thunder.png", 64, 64, "Start a thunder light effect"));
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
        .appendField(new Blockly.FieldImage("assets/images/light_wave.png", 64, 64, "Start a breathing light effect"));
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
        .appendField(new Blockly.FieldImage("assets/images/camera_pan_left.png", 64, 64, "Camera Pan Left"));
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
        .appendField(new Blockly.FieldImage("assets/images/camera_pan_right.png", 64, 64, "Camera Pan Right"));
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
        .appendField(new Blockly.FieldImage("assets/images/camera_tilt_up.png", 64, 64, "Camera Tilt Up"));
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
        .appendField(new Blockly.FieldImage("assets/images/camera_tilt_down.png", 64, 64, "Camera Tilt Down"));
    this.setPreviousStatement(true, null);
    this.setNextStatement(true, null);
    this.setColour(165);
 this.setTooltip("Pivote la camera vers le bas");
 this.setHelpUrl("");
  }
};



