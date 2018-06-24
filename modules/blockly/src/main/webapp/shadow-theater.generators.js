function getGlobalVariablesStatement(block, addLineReturn) {
  if(addLineReturn=== undefined) {
    addLineReturn = true;
  }
  // taken from blockly/blob/master/generators/python/procedures.js
  var globals = [];
  var workspace = block.workspace;
  var varName;
  var variables = workspace.getAllVariables() || [];
  for (var i = 0, variable; variable = variables[i]; i++) {
    varName = variable.name;
//    if (block.arguments_.indexOf(varName) == -1) {
      globals.push(Blockly.Python.variableDB_.getName(varName,
          Blockly.Variables.NAME_TYPE));
//    }
  }
  var globals = globals.length ? '  global ' + globals.join(', ') + (addLineReturn?'\n':'') : '';
  return globals;
}

var sounds = ["water","fire","soil","wood","metal"];
for(var i=0, len=sounds.length; i < len; i++){
   Blockly.Python[('sound_'+sounds[i])] = eval("function(block) {"
    +"var code = \'drivar.sound_playAsync(\"sound_"+sounds[i]+"\")\\n\';"
    + "return code;}");
};

Blockly.Python['motor_forward'] = function(block) {
  var code = 'drivar.motor_move()\n'
             +'drivar_motion_compensate+=1\n';
  return code;
};
Blockly.Python['motor_backward'] = function(block) {
  var code = 'drivar.motor_move(Drivar.DIR_BACKWARD)\n'
             +'drivar_motion_compensate-=1\n';
  return code;
};
Blockly.Python['motor_full_turn'] = function(block) {
  var code = 'drivar.motor_turn(angle=360)\n';
  return code;
};

Blockly.Python['light_on'] = function(block) {
  var code = 'drivar.spotlight_setIntensity(100)\n';
  return code;
};
Blockly.Python['light_off'] = function(block) {
  var code = 'drivar.spotlight_setIntensity(0)\n';
  return code;
};
Blockly.Python['light_thunder'] = function(block) {
  var code = 'drivar.spotlight_thunder()\n';
  return code;
};
Blockly.Python['light_wave'] = function(block) {
  var code = 'drivar.spotlight_wave()\n';
  return code;
};

Blockly.Python['camera_pan_left'] = function(block) {
  var code = 'drivar.camera_panTo(0)\n';
  return code;
};

Blockly.Python['camera_pan_right'] = function(block) {
  var code = 'drivar.camera_panTo(90)\n';
  return code;
};

Blockly.Python['camera_tilt_up'] = function(block) {
  var code = 'drivar.camera_tiltTo(90)\n';
  return code;
};

Blockly.Python['camera_tilt_down'] = function(block) {
  var code = 'drivar.camera_tiltTo(0)\n';
  return code;
};
