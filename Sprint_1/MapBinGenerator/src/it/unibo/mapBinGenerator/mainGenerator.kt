package it.unibo.mapBinGenerator

import mapRoomKotlin.mapUtil
import aima.core.agent.Action

object generatorMap {
    fun generateMap(nColumn : Int, nRaw: Int, obstacles : ArrayList<Pair<Int,Int>>, nameFile : String) {
        try {
			itunibo.planner.plannerUtil.initAI()
			itunibo.planner.plannerUtil.showMap()

			println("Mapping STARTS")
			drawTable(nColumn, nRaw, obstacles)
			putObstacles(obstacles)
			goto(0,0)
			itunibo.planner.plannerUtil.showMap()
            itunibo.planner.plannerUtil.saveRoomMap(nameFile)
		} catch (e: Exception) {
            e.printStackTrace()
        }
    }
	
    fun goto(x : Int, y: Int){
    	var moves : List<Action>?
		itunibo.planner.plannerUtil.setGoal(x,y)
		moves = itunibo.planner.plannerUtil.doPlan()
		for(m in moves!!){
			itunibo.planner.plannerUtil.updateMap(m.toString())
		}
	}

/* To mark obstacles in the map*/
    internal fun putObstacles(obstacles : ArrayList<Pair<Int,Int>>){
		var moves : List<Action>?
		for(goal in obstacles){
			goto(goal.first,goal.second)
			itunibo.planner.plannerUtil.updateMap("s")
			itunibo.planner.plannerUtil.updateMapObstacleOnCurrentDirection()
		}
	}

/* To draw a column of free cell ('1')*/
    internal fun drawColumn(len: Int, curCol: Int) {
    	var i = 0
		var t1 = ""
		var t2 = ""
		var obsY = 0
		while(i < len-1){
// Search for one obstacle
//			if(curCol%2==0){
//					//down direction
//					t1 = "r"
//					t2 = "l"
//					obsY = obstacleY
//				}else{
//					//up direction
//					t1 = "l"
//					t2 = "r"
//					obsY = len-obstacleY
//				}
//			if( curCol == obstacleX && i==obsY){
//				itunibo.planner.plannerUtil.showCurrentRobotState()
//				itunibo.planner.plannerUtil.updateMapObstacleOnCurrentDirection()
//				itunibo.planner.plannerUtil.updateMap(t1)
//				itunibo.planner.plannerUtil.updateMap("w")
//				itunibo.planner.plannerUtil.updateMap(t2)
//				itunibo.planner.plannerUtil.updateMap("w")
//				itunibo.planner.plannerUtil.updateMap("w")
//				itunibo.planner.plannerUtil.updateMap(t2)
//				itunibo.planner.plannerUtil.updateMap("w")
//				itunibo.planner.plannerUtil.updateMap(t1)
//				itunibo.planner.plannerUtil.showCurrentRobotState()
//				i++
//			}else{
				itunibo.planner.plannerUtil.updateMap("w")
//			}
			i++
		
		}
	}

/* To draw a map of free cells ('1')*/
	internal fun drawTable(nColumn : Int, nRaw: Int, obstacles : ArrayList<Pair<Int,Int>>){
		var i = 0
		var dir = ""
		var cul = nColumn -1
		while(i < nColumn){
			drawColumn( nRaw, i)
			if(i< cul){
				if(i%2==0){
					
					//Turn up robot face
					dir = "l"
				}else{
					//Turn down robot face
					dir = "r"
				}
					itunibo.planner.plannerUtil.updateMap(dir)
					itunibo.planner.plannerUtil.updateMap("w")
					itunibo.planner.plannerUtil.updateMap(dir)
				}
			i++
		}
	}

    @JvmStatic
    fun main(args: Array<String>) {
		var obst : ArrayList<Pair<Int,Int>> = ArrayList<Pair<Int,Int>>()
		var p1 = Pair<Int,Int>(2,3)
		var p2 = Pair<Int,Int>(3,3)
		var p3 = Pair<Int,Int>(4,3)

		obst.add(p1)
		obst.add(p2)
		obst.add(p3)
        generateMap(7,7,obst,"roomMap")
    }
}
