    var scene, camera,topCamera, controls, stats;
    var renderer;
    var gianoPiSpeed = 2;
    var url = 'scene.json';
	var container = document.getElementById( 'simulation-div' );
    var SCREEN_W = 480;
	var SCREEN_H = 750;
    var gpi,gpiSpotlight,gpiCamera, gpiDirection;
    var gpiCurrentPos = new THREE.Vector3();
    
    function initialize_simulator(){
	    renderer = new THREE.WebGLRenderer( { antialias: true } );
	    renderer.setPixelRatio( window.devicePixelRatio );
	    renderer.setSize( SCREEN_W, SCREEN_H );
	    renderer.shadowMap.enabled = true;
	    renderer.shadowMapSoft = false;
	    container.appendChild( renderer.domElement );
	    
	    // Load a scene with objects, lights and camera from a JSON file
	    new THREE.ObjectLoader().load( url, function ( loadedScene ) {
	      scene = loadedScene;
	      scene.background = new THREE.Color( 0xffffff );
	      // create a new downwards looking camera and use it in the loaded scene
	      if ( topCamera === undefined ) {
	        topCamera = new THREE.PerspectiveCamera( 30, (SCREEN_W / (SCREEN_H/2) ), 1, 2000 );
	        topCamera.position.set( 0, 50, 0 );
	        topCamera.setFocalLength(34);
	        topCamera.lookAt(new THREE.Vector3(0,0,0));
	        camera = topCamera;
	      }
	      gpi = scene.getObjectByName("GianoPi");
	      gpiDirection = gpi.getWorldDirection();
	      gpiSpotlight = scene.getObjectByName("GianoPi SpotLight");
	      
	      //
	      gpiCamera = scene.getObjectByName("GianoPi Camera");
	      gpiCamera.setFocalLength(20);
	      gpiSpotlight.target = gpiCamera;
	      //gianoPiSpotlight.intensity = 0;
	      controls = new THREE.OrbitControls( camera,container );
	      
	      // Ground plane and fog: examples for applying additional children and new property values to the loaded scene
	      var geometry = new THREE.PlaneBufferGeometry( 40000, 40000 );
	      var material = new THREE.MeshPhongMaterial( { shininess: 0.1 } );
	      var ground = new THREE.Mesh( geometry, material );
	      ground.position.set( 0, - 250, 0 );
	      ground.rotation.x = - Math.PI / 2;
	      scene.add( ground );
	      scene.fog = new THREE.Fog( 0xffffff, 1000, 10000 );
	      animate();
	      // Make bigger shadows by offsetting the viewport
	      gpiSpotlight.shadow.map.viewport.copy({x:-20,y:-20,w:600,z:600});
	      controls.maxPolarAngle = (Math.PI/2)-0.2;
	    } );
    }
    
    window.onresize = function () {
	  topCamera.aspect = (SCREEN_W / (SCREEN_H/2) );
      gpiCamera.aspect = (SCREEN_W / (SCREEN_H/2) );
      topCamera.updateProjectionMatrix();
      gpiCamera.updateProjectionMatrix();
      renderer.setSize(SCREEN_W, SCREEN_H );
    };
    
    function animate() {
      
	  requestAnimationFrame( animate );
      TWEEN.update();
      controls.update();
      
      render();
    }
    
    function render() {
	    var left,bottom,width,height;

	    left = 1; bottom = 1; width = SCREEN_W-2; height = 0.5 * SCREEN_H-2;
	    renderer.setViewport (left,bottom,width,height);
	    renderer.setScissor(left,bottom,width,height);
	    renderer.setScissorTest (true);
	    topCamera.aspect = width/height;
	    topCamera.updateProjectionMatrix();
	    renderer.render (scene,topCamera);
        
	    left = 1; bottom = 0.5*SCREEN_H+1; width = SCREEN_W-2; height = 0.5 * SCREEN_H-2;
		renderer.setViewport (left,bottom,width,height);
		renderer.setScissor(left,bottom,width,height);
		renderer.setScissorTest (true);  // clip out "viewport"
		gpiCamera.aspect = width/height;
		gpiCamera.updateProjectionMatrix();
		renderer.render (scene,gpiCamera);  
		  
        
	    
	  
    }
    
    function moveRobotTo(destination, callback){
    	
//    	var listener = new THREE.AudioListener();
//    	gpiCamera.add( listener );
    	// create a global audio source
//    	var sound = new THREE.Audio( listener );
//
//    	// load a sound and set it as the Audio object's buffer
//    	var audioLoader = new THREE.AudioLoader();
//    	audioLoader.load( 'blockly/assets/sounds/swamp.mp3', function( buffer ) {
//    		sound.setBuffer( buffer );
//    		sound.setLoop( false );
//    		sound.setVolume(1 );
//    		sound.play();
//    	});
    	
    	
        var thisCallback = callback;
    	tweenGianoPiToLookAt(destination, function(){
    		tweenGianoPiToDestination(destination, thisCallback);
    	});
    }

    function tweenGianoPiToLookAt(destination, callback){
    	var startRotation = new THREE.Euler().copy( gpi.rotation );

    	// final rotation (with lookAt)
    	gpi.lookAt( destination.position );
    	var endRotation = new THREE.Euler().copy( gpi.rotation );

    	// revert to original rotation
    	gpi.rotation.copy( startRotation );
    	var duration = 500;

    	// Tween
    	new TWEEN.Tween( gpi.rotation )
    	         .to( {y: endRotation.y}, duration )
    	         .easing(TWEEN.Easing.Quadratic.InOut)
    	         .onComplete(function() {
    	        	 console.log("rotation complete");
    	        	 if(callback) callback();
    	        	})
    	         .start();
    	return duration;
      }
    
    function tweenGianoPiToDestination(destination, callback){
      gpiCurrentPos.copy(gpi.position);
      
      var distance = gpiCurrentPos.distanceTo(destination.position);
      var duration = (distance / gianoPiSpeed) * 1000;
      
      new TWEEN.Tween(gpi.position)
        .to(destination.position, duration)
        .easing(TWEEN.Easing.Quadratic.InOut)
        .onComplete(function() {
          console.log("movement complete");
          if(callback) callback();
        })
        .start();
      return duration;
    }
