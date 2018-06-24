    var scene, camera,topCamera, controls, stats;
    var renderer;
    var gianoPiSpeed = 5;
    var url = 'scene.json';
    var SCREEN_WIDTH = window.innerWidth;
    var SCREEN_HEIGHT = window.innerHeight;
    var container = document.getElementById( 'simulation-div' );
    var gpi,gpiSpotlight,gpiCamera;
    var gpiCurrentPos = new THREE.Vector3();
    
    function initialize_simulator(){
	    renderer = new THREE.WebGLRenderer( { antialias: true } );
	    renderer.setPixelRatio( window.devicePixelRatio );
	    renderer.setSize( ($(container).width()-10), SCREEN_HEIGHT/2 );
	    renderer.shadowMap.enabled = true;
	    renderer.shadowMapSoft = false;
	    container.appendChild( renderer.domElement );
	    
	    // Load a scene with objects, lights and camera from a JSON file
	    new THREE.ObjectLoader().load( url, function ( loadedScene ) {
	      scene = loadedScene;
	      scene.background = new THREE.Color( 0xffffff );
	      // create a new downwards looking camera and use it in the loaded scene
	      if ( topCamera === undefined ) {
	        topCamera = new THREE.PerspectiveCamera( 30, SCREEN_WIDTH / SCREEN_HEIGHT, 1, 2000 );
	        topCamera.position.set( 0, 50, 0 );
	        topCamera.setLens(34);
	        topCamera.lookAt(new THREE.Vector3(0,0,0));
	        camera = topCamera;
	      }
	      gpi = scene.getObjectByName("GianoPi");
	      gpiSpotlight = scene.getObjectByName("GianoPi SpotLight");
	      
	      //
	      gpiCamera = scene.getObjectByName("GianoPi Camera");
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
      camera.aspect = window.innerWidth / window.innerHeight;
      camera.updateProjectionMatrix();
      renderer.setSize( ($(container).width()-10), window.innerHeight/2 );
    };
    
    function animate() {
      requestAnimationFrame( animate );
      TWEEN.update();
      controls.update();
      renderer.render( scene, camera );
      
    }
    

    function tweenGianoPiTo(destination){
      gpiCurrentPos.copy(gpi.position);
      var distance = gpiCurrentPos.distanceTo(destination.position);
      var duration = (distance / gianoPiSpeed) * 1000;
      
      new TWEEN.Tween(gpi.position)
        .to(destination.position, duration)
        .easing(TWEEN.Easing.Quadratic.InOut)
        .onComplete(function() {
          console.log("movement complete")
        })
        .start();
    }
