const config = {
    floor: {
        size: { x: 33.75, y: 33.75 }
        /* map of 9x9 cells of dim 3.75;
           rbr can walk in floor size is 7x7 cells of dim 3.75 */
    },
    
    player: {
    	/* position in interval [0, 1]. --> so if in cell (0, 0) in 7x7 map: the center is in 0.5 / 7 =~ 0.07 */
        position: { x: 0.17, y: 0.17 },		// center of RH cell (1.5, 1.5) / 9 =~ (0.17, 0.17)
        /* speed in interval [0, 100] */
        speed: 0.2
    },
    
    sonars: [
	/*
        {
            name: "sonar1",
            position: { x: 0.12, y: 0.05 },
            senseAxis: { x: false, y: true }
        },
       {
            name: "sonar2",
            position: { x: 0.94, y: 0.88},
            senseAxis: { x: true, y: false }
        }
	*/
    ],
      
    movingObstacles: [
		{
            name: "movingobstacle",
            //position: { x: .64, y: .42 },
            position: { x: 0, y: .42 },	//--> to fix if out of the room
            directionAxis: { x: true, y: true },
            //speed: 0.2,
            speed: 0.1, //--> to stay in the room
            //range: 28 
            range: 0	//--> to fix if out of the room
            //range: 7 //--> to stay in the room
        },
 
        {
            name: "wall",
            //position: { x: 0.0, y: 0.6 },
            //position: { x: 0.5, y: 0.6 },	//--> to stay in the room
            position: { x: 0, y: 0.6 },	//--> to fix if out of the room
            directionAxis: { x: true, y: false },
            //speed: 0.0078,
            speed: 0.07,	//--> to stay in the room
            //range: 120
            //range: 12	//--> to stay in the room          
            range: 0	//--> to fix if out of the room
        }
    ],
    
   staticObstacles: [
        {
            name: "fridge",
            centerPosition: { x: 0.78, y: 0.97 },		// (7, 8.25) / 9 =~ (0.78, 0.97)
            size: { x: 0.22, y: 0.06 }					// dim: 2 x 0.5 (if /9 =~ (0.22, 0.06))
        },

        {
            name: "dishwasher",
            centerPosition: { x: 0.78, y: 0.03 },		// (7, 0.25) / 9 =~ (0.78, 0.03)
            size: { x: 0.22, y: 0.06 }					// dim: 2 x 0.5 (if /9 =~ (0.22, 0.06))
		},
		
        {
            name: "pantry",
            centerPosition: { x: 0.22, y: 0.03 },		// (2, 0.25) / 9 =~ (0.22, 0.03)
            size: { x: 0.22, y: 0.06 }					// dim: 2 x 0.5 (if /9 =~ (0.22, 0.06))
		},
		
        {
            name: "table",								
            centerPosition: { x: 0.5, y: 0.5 },			// (4.5, 4.5) / 9 =~ (0.5, 0.5)
            //size: { x: 0.33, y: 0.1 }					// dim: 3 x 1 (se /9 =~ (0.33, 0.1))
            size: { x: 0.31, y: 0.09 }					// dim: 2.8 x 0.8 (se /9 =~ (0.31, 0.09))
		},

        {
        	name: "wallUp",
			centerPosition: { x: 0.5, y: 0.92 },		// (4.5, 8.25) / 9 =~ (0.5, 0.92)
			size: { x: 0.9, y: 0.06 }					// dim: 8 x 0.5 (se /9 =~ (0.9, 0.06))
        },
        
        {
            name: "wallDown",
            centerPosition: { x: 0.5, y: 0.08 },		// (4.5, 0.75) / 9 =~ (0.5, 0.08)
            size: { x: 0.9, y: 0.06 }					// dim: 8 x 0.5 (se /9 =~ (0.9, 0.06))
        },
        
        {
            name: "wallLeft",
            centerPosition: { x: 0.08, y: 0.5 },		// (0.75, 4.5) / 9 =~ (0.08, 0.5)
            size: { x: 0.06, y: 0.9 }					// dim: 0.5 x 8 (se /9 =~ (0.06, 0.9))
        },
        
        {
            name: "wallRight",
            centerPosition: { x: 0.92, y: 0.5 },		// (8.25, 4.5) / 9 =~ (0.92, 0.5)
            size: { x: 0.06, y: 0.9 }					// dim: 0.5 x 8 (se /9 =~ (0.06, 0.9))
        }
    ]
}

export default config;