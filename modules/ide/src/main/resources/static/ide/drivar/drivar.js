/* **************************************
 *  Javascript THREE Drivar implementation.
 *  Provides handy Javascript functions to move the robot.
 * **************************************/

function Drivar(){
   this.moving = false;
   this.robot = null;
   this.robotSpotlight = null;
   this.robotCamera = null;
   this.robotCurrentPos = new THREE.Vector3();
   this.robotSpeed = 2;
   
   this.scene = null;
   this.topCamera = null;
   this.SCREEN_W = null;
   this.SCREEN_H = null;
   this.initialized = false;
   this.renderer = null;
   this.container = null;
   
//   this.animate = this.animate.bind(this);
}

Drivar.prototype.initialize = function(sceneUrl, container, screenWidth = 480, screenHeight=750, activateAntialias = true){
	this.SCREEN_W = screenWidth;
	this.SCREEN_H = screenHeight;
	this.container = container;
	this.renderer = new THREE.WebGLRenderer( { antialias: activateAntialias } );
	this.renderer.setPixelRatio( window.devicePixelRatio );
    this.renderer.setSize( this.SCREEN_W, this.SCREEN_H );
    this.renderer.shadowMap.enabled = true;
    this.renderer.shadowMapSoft = false;
    container.appendChild( this.renderer.domElement );
    
    var that = this;
    
    new THREE.ObjectLoader().load( sceneUrl, function ( loadedScene ) {
    	that.scene = loadedScene;
    	that.scene.background = new THREE.Color( 0xffffff );
	      // create a new downwards looking camera and use it in the loaded scene
	      if ( that.topCamera == null) {
	    	  that.topCamera = new THREE.PerspectiveCamera( 30, (that.SCREEN_W / (that.SCREEN_H/2) ), 1, 2000 );
	    	  that.topCamera.position.set( 0, 50, 0 );
	    	  that.topCamera.setFocalLength(34);
	    	  that.topCamera.lookAt(new THREE.Vector3(0,0,0));
	        
	      }
	      that.robot = that.scene.getObjectByName("Robot");
	      that.robotSpotlight = that.scene.getObjectByName("Robot SpotLight");
	      
	      //
	      that.robotCamera = that.scene.getObjectByName("Robot Camera");
	      that.robotCamera.setFocalLength(20);
	      that.robotSpotlight.target = that.robotCamera;
	      that.controls = new THREE.OrbitControls( that.topCamera,that.container );
	      
	      var geometry = new THREE.PlaneBufferGeometry( 40000, 40000 );
	      var material = new THREE.MeshPhongMaterial( { shininess: 0.1 } );
	      var ground = new THREE.Mesh( geometry, material );
	      ground.position.set( 0, - 250, 0 );
	      ground.rotation.x = - Math.PI / 2;
	      that.scene.add( ground );
	      that.scene.fog = new THREE.Fog( 0xffffff, 1000, 10000 );
	      that.animate();
	      // Make bigger shadows by offsetting the viewport
	      that.robotSpotlight.shadow.map.viewport.copy({x:-20,y:-20,w:600,z:600});
	      that.controls.maxPolarAngle = (Math.PI/2)-0.2;
	    } );
    
	this.initialized = true;
}


Drivar.prototype.animate = function(){
	// TODO : use bind to animate
	requestAnimationFrame( drivar.animate );
    TWEEN.update();
    drivar.controls.update();
    
    drivar.render();
}

Drivar.prototype.render = function() {
    var left,bottom,width,height;

    left = 1; bottom = 1; width = this.SCREEN_W-2; height = 0.5 * this.SCREEN_H-2;
    this.renderer.setViewport (left,bottom,width,height);
    this.renderer.setScissor(left,bottom,width,height);
    this.renderer.setScissorTest (true);
    this.topCamera.aspect = width/height;
    this.topCamera.updateProjectionMatrix();
    this.renderer.render (this.scene,this.topCamera);
    
    left = 1; bottom = 0.5*this.SCREEN_H+1; width = this.SCREEN_W-2; height = 0.5 * this.SCREEN_H-2;
    this.renderer.setViewport (left,bottom,width,height);
    this.renderer.setScissor(left,bottom,width,height);
    this.renderer.setScissorTest (true);  // clip out "viewport"
    this.robotCamera.aspect = width/height;
    this.robotCamera.updateProjectionMatrix();
    this.renderer.render (this.scene,this.robotCamera);  
}

Drivar.prototype.moveRobotTo = function (destination, callback){
	this.moving = true;
	var that = this;
	var thisCallback = callback;
	this._tweenGianoPiToLookAt(destination, function(){
		that._tweenGianoPiToDestination(destination, thisCallback);
		that.moving = false;
	});
}


Drivar.prototype._tweenGianoPiToLookAt = function(destination, callback){
	var startRotation = new THREE.Euler().copy( this.robot.rotation );

	// final rotation (with lookAt)
	this.robot.lookAt( destination.position );
	var endRotation = new THREE.Euler().copy( this.robot.rotation );

	// revert to original rotation
	this.robot.rotation.copy( startRotation );
	var duration = 500;

	// Tween
	new TWEEN.Tween( this.robot.rotation )
	         .to( {y: endRotation.y}, duration )
	         .easing(TWEEN.Easing.Quadratic.InOut)
	         .onComplete(function() {
	        	 console.log("rotation complete");
	        	 if(callback) callback();
	        	})
	         .start();
	return duration;
}

Drivar.prototype._tweenGianoPiToDestination = function(destination, callback){
	this.robotCurrentPos.copy(this.robot.position);
    
    var distance = this.robotCurrentPos.distanceTo(destination.position);
    var duration = (distance / this.robotSpeed) * 1000;
    
    new TWEEN.Tween(this.robot.position)
      .to(destination.position, duration)
      .easing(TWEEN.Easing.Quadratic.InOut)
      .onComplete(function() {
        console.log("movement complete");
        if(callback) callback();
      })
      .start();
    return duration;
  }

drivar = new Drivar();