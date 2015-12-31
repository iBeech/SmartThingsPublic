/**
 *  British Gas myHome
 *
 *  Copyright 2015 Alex Lee Yuk Cheung, modified by Thomas Beech to work with British Gas myHome device
 *
 * 	1. Create a new device type (https://graph.api.smartthings.com/ide/devices)
 *     Name: British Gas myHome
 *     Author: iBeech
 *     Capabilities:
 *         Polling
 *         Refresh
 *         Temperature Measurement
 *		   Thermostat
 *         Thermostat Mode
 *         Thermostat Operating State
 *		   Thermostat Heating Setpoint
 *
 *     Custom Commands:
 *         setThermostatMode
 *         setHeatingSetpoint
 *         heatingSetpointUp
 *         heatingSetpointDown
 *
 * 	2. Create a new device (https://graph.api.smartthings.com/device/list)
 *     Name: Your Choice
 *     Device Network Id: Your Choice
 *     Type: Hive Active Heating (should be the last option)
 *     Location: Choose the correct location
 *     Hub/Group: Leave blank
 *
 * 	3. Update device preferences
 *     Click on the new device to see the details.
 *     Click the edit button next to Preferences
 *     Fill in your your Hive user name, Hive password.
 *
 *	4. ANDROID USERS - You have to comment out the iOS details line at line 205 by adding "//" 
 * 	   and uncomment the Android details line by removing the preceding "//" at line 213 before publishing.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 *
 *	VERSION HISTORY
 *  31.12.2015
 *	v1.0 - Initial Release

 */
preferences {
	input("username", "text", title: "Username", description: "Your Hive username (usually an email address)")
	input("password", "password", title: "Password", description: "Your Hive password")
} 
 
metadata {
	definition (name: "British Gas myHome", namespace: "iBeech", author: "Thomas Beech") {
		capability "Actuator"
		capability "Polling"
		capability "Refresh"
		capability "Temperature Measurement"
		capability "Thermostat"
		capability "Thermostat Heating Setpoint"
		capability "Thermostat Mode"
		capability "Thermostat Operating State"
		
		command "heatingSetpointUp"
		command "heatingSetpointDown"
		command "setThermostatMode"
		command "setHeatingSetpoint"
	}

	simulator {
		// TODO: define status and reply messages here
	}

	tiles(scale: 2) {

		multiAttributeTile(name: "thermostat", width: 6, height: 4, type:"thermostat") {
			tileAttribute("device.temperature", key:"PRIMARY_CONTROL", canChangeBackground: true){
				attributeState "default", label: '${currentValue}°', unit:"C", backgroundColors: [
				// Celsius Color Range
				[value: 0, color: "#50b5dd"],
				[value: 10, color: "#43a575"],
				[value: 13, color: "#c5d11b"],
				[value: 17, color: "#f4961a"],
				[value: 20, color: "#e75928"],
				[value: 25, color: "#d9372b"],
				[value: 29, color: "#b9203b"]
			]}
			tileAttribute ("hiveHeating", key: "SECONDARY_CONTROL") {
				attributeState "hiveHeating", label:'${currentValue}'
			}
		}
		
		valueTile("thermostat_small", "device.temperature", width: 4, height: 4) {
			state "default", label:'${currentValue}°', unit:"C",
			backgroundColors:[
				[value: 0, color: "#50b5dd"],
				[value: 10, color: "#43a575"],
				[value: 13, color: "#c5d11b"],
				[value: 17, color: "#f4961a"],
				[value: 20, color: "#e75928"],
				[value: 25, color: "#d9372b"],
				[value: 29, color: "#b9203b"]
			]
		}
		
		standardTile("thermostat_main", "device.thermostatOperatingState", inactiveLabel: true, decoration: "flat", width: 2, height: 2) {
			state "idle", label:'${currentValue}', icon: "st.Weather.weather2"
			state "heating", label:'${currentValue}', icon: "st.Weather.weather2", backgroundColor:"#EC6E05"
		}
		
		controlTile("heatSliderControl", "device.heatingSetpoint", "slider", height: 2, width: 4, inactiveLabel: false, range:"(5..32)") {
			state "setHeatingSetpoint", label:'Set temperature to', action:"setHeatingSetpoint"
		}
		
		standardTile("heatingSetpointUp", "device.heatingSetpoint", width: 2, height: 2, canChangeIcon: false, inactiveLabel: false, decoration: "flat") {
			state "heatingSetpointUp", label:'  ', action:"heatingSetpointUp", icon:"st.thermostat.thermostat-up", backgroundColor:"#ffffff"
		}

		standardTile("heatingSetpointDown", "device.heatingSetpoint", width: 2, height: 2, canChangeIcon: false, inactiveLabel: false, decoration: "flat") {
			state "heatingSetpointDown", label:'  ', action:"heatingSetpointDown", icon:"st.thermostat.thermostat-down", backgroundColor:"#ffffff"
		}

		valueTile("heatingSetpoint", "device.heatingSetpoint", width: 2, height: 2) {
			state "default", label:'${currentValue}°', unit:"C",
			backgroundColors:[
				[value: 0, color: "#50b5dd"],
				[value: 10, color: "#43a575"],
				[value: 13, color: "#c5d11b"],
				[value: 17, color: "#f4961a"],
				[value: 20, color: "#e75928"],
				[value: 25, color: "#d9372b"],
				[value: 29, color: "#b9203b"]
			]
		}
   
		standardTile("thermostatOperatingState", "device.thermostatOperatingState", inactiveLabel: true, decoration: "flat", width: 2, height: 2) {
			state "idle", action:"polling.poll", label:'${name}', icon: "st.sonos.pause-icon"
			state "heating", action:"polling.poll", label:'  ', icon: "st.thermostat.heating", backgroundColor:"#EC6E05"
		}
		
		standardTile("thermostatMode", "device.thermostatMode", inactiveLabel: true, decoration: "flat", width: 2, height: 2) {
			state("auto", label: "SCHEDULED", icon:"st.Office.office7")
			state("off", label: "OFF", icon:"st.thermostat.heating-cooling-off")
			state("heat", label: "MANUAL", icon:"st.Weather.weather2")
		}

		standardTile("refresh", "device.refresh", inactiveLabel: false, decoration: "flat", width: 2, height: 2) {
			state("default", label:'refresh', action:"polling.poll", icon:"st.secondary.refresh-icon")
		}
		
		standardTile("mode_auto", "device.mode_auto", inactiveLabel: false, decoration: "flat", width: 2, height: 2) {
			state "default", action:"auto", label:'Schedule', icon:"st.Office.office7"
		}
		
		standardTile("mode_manual", "device.mode_manual", inactiveLabel: false, decoration: "flat", width: 2, height: 2) {
			state "default", action:"heat", label:'Manual', icon:"st.Weather.weather2"
		}
		
		standardTile("mode_off", "device.mode_off", inactiveLabel: false, decoration: "flat", width: 2, height: 2) {
			state "default", action:"off", icon:"st.thermostat.heating-cooling-off"
		}

		main(["thermostat_main"])

		// ============================================================
		// iOS TILES
		// To expose iOS optimised tiles, comment out the details line in Android Tiles section and uncomment details line below.
		
		details(["thermostat", "mode_auto", "mode_manual", "mode_off", "heatingSetpoint", "heatSliderControl", "refresh"])
		
		// ============================================================

		// ============================================================
		// ANDROID TILES
		// To expose Android optimised tiles, comment out the details line in iOS Tiles section and uncomment details line below.
		
		//details(["thermostat_small", "thermostatOperatingState", "thermostatMode", "mode_auto", "mode_manual", "mode_off", "heatingSetpoint", "heatSliderControl", "refresh"])
		
		// ============================================================
	}
}

// parse events into attributes
def parse(String description) {
	log.debug "Parsing '${description}'"
	// TODO: handle 'temperature' attribute
	// TODO: handle 'heatingSetpoint' attribute
	// TODO: handle 'thermostatSetpoint' attribute
	// TODO: handle 'thermostatMode' attribute
	// TODO: handle 'thermostatOperatingState' attribute
}

// handle commands
def setHeatingSetpoint(temp) {
	log.debug "Executing 'setHeatingSetpoint with temp $temp'"
	def latestThermostatMode = device.latestState('thermostatMode')
	
	if (temp < 5) {
		temp = 5
	}
	if (temp > 32) {
		temp = 32
	}
	
	
	//if thermostat is off, set to manual    
	if (latestThermostatMode.stringValue == 'off') {
		
        api('thermostat_mode',  args) {
		}    	
	}  
     
	api('temperature', "temperature=" + temp + "&temperatureUnit=C"	) {        
		runIn(1, poll)   
	}	
}

def heatingSetpointUp(){
	log.debug "Executing 'heatingSetpointUp'"
	int newSetpoint = device.currentValue("heatingSetpoint") + 1
	log.debug "Setting heat set point up to: ${newSetpoint}"
	setHeatingSetpoint(newSetpoint)
}

def heatingSetpointDown(){
	log.debug "Executing 'heatingSetpointDown'"
	int newSetpoint = device.currentValue("heatingSetpoint") - 1
	log.debug "Setting heat set point down to: ${newSetpoint}"
	setHeatingSetpoint(newSetpoint)
}

def off() {
	setThermostatMode('off')
}

def heat() {
	setThermostatMode('heat')
}

def auto() {
	setThermostatMode('auto')
}

def setThermostatMode(mode) {

	mode = mode == 'cool' ? 'heat' : mode
	log.debug "Executing 'setThermostatMode with mode $mode'"
	
    def args = ""
	if (mode == 'off') {
		args = "control=OFF&confirmed=true"
	} else if (mode == 'heat') {
		args = "control=MANUAL&confirmed=true"
	} else {
        args = "control=SCHEDULE&confirmed=true"
    }
	api('thermostat_mode',  args) {
		mode = mode == 'range' ? 'auto' : mode
		runIn(1, poll)
	}
}

def poll() {
	log.debug "Executing 'poll'"    
	api('status', [], {
				
        log.debug data
        
		//Construct status message
		def statusMsg = "Currently"
			
		// get temperature status
		def temperature = (float)data.nodes.currentTemperature
        
		def heatingSetpoint = 7.0
        if(data.nodes.targetTemperature != "--") {
        	heatingSetpoint = (float)data.nodes.targetTemperature
        }
        
		temperature = String.format("%2.1f", temperature)
		heatingSetpoint = convertTemperatureIfNeeded(heatingSetpoint, data.nodes.formatting.temperatureUnit, 1)
		
		// convert temperature reading of 1 degree to 7 as Hive app does
		if (heatingSetpoint == "1.0") {
			heatingSetpoint = "7.0"
		}
		
		sendEvent(name: 'temperature', value: temperature, unit: data.nodes.formatting.temperatureUnit, state: "heat")
		sendEvent(name: 'heatingSetpoint', value: heatingSetpoint, unit: data.nodes.formatting.temperatureUnit, state: "heat")
		sendEvent(name: 'thermostatSetpoint', value: heatingSetpoint, unit: data.nodes.formatting.temperatureUnit, state: "heat", displayed: false)
               		 
        // Can be: MANUAL / OFF / SCHEDULE / OVERRIDE
        def activeControl = data.nodes.control
        log.debug activeControl

        def mode = 'auto';
        
        switch(activeControl)
        {
        	case "OFF": 
            	statusMsg = statusMsg + " set to OFF"                
                mode = 'off'
            break
            
            case "SCHEDULE": 
            	statusMsg = statusMsg + " set to SCHEDULE"
                mode = 'auto'
            break
            
            case "MANUAL": 
            	statusMsg = statusMsg + " set to MANUAL"
                mode = 'heat'
            break
            
            case "OVERRIDE":
            	statusMsg = "Schedule overridden"
                mode = 'auto'
            break
        }       
        
        log.debug "thermostatMode $mode"
        sendEvent(name: 'thermostatMode', value: mode) 
        
		// Can be: 'on' for heating / 'off' when not heating
        def boilerActive = data.nodes.active
		log.debug "boilerActive: $boilerActive"
		
		if (boilerActive) {
			sendEvent(name: 'thermostatOperatingState', value: "heating")
			statusMsg = statusMsg + " and is HEATING"
		}       
		else {
			sendEvent(name: 'thermostatOperatingState', value: "idle")
			statusMsg = statusMsg + " and is IDLE"
		}
			   
		sendEvent("name":"hiveHeating", "value": statusMsg, displayed: false)        
	})
}

def refresh() {
	log.debug "Executing 'refresh'"
	poll()
}

def api(method, args, success = {}) {
	log.debug "Executing 'api'"
	
	if(!isLoggedIn()) {
		log.debug "Need to login"
		login(method, args, success)
		return
	}    
    
    log.debug data
    def thermostat_mode_uri = "https://api.hivehome.com/v5/users/" + settings.username + "/widgets/climate/"
    
    if(data.nodes != null) {    
    	thermostat_mode_uri += data.nodes.deviceId + "/control"
    }
	
	def methods = [
		'status': [uri: "https://api.hivehome.com/v5/users/" + settings.username + "/widgets/climate", type: 'get'],
		'temperature': [uri: "https://api.hivehome.com/v5/users/" + settings.username + "/widgets/climate/targetTemperature", type: 'put'],
		'thermostat_mode': [uri: thermostat_mode_uri, type: 'put']
	]
    
	def request = methods.getAt(method)
	
	log.debug "Starting $method : $args"
	doRequest(request.uri, args, request.type, success)
}

// Need to be logged in before this is called. So don't call this. Call api.
def doRequest(uri, args, type, success) {
	log.debug "Calling doRequest()"
	log.debug "Calling $type : $uri : $args"
	
	def params = [
		uri: uri,
		contentType: 'application/json',
		headers: [
			  'Cookie': state.cookie,
			  'Content-Type': 'application/json',
              'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8',
              'Accept-Encoding': 'gzip, deflate, sdch'
		],
		body: args
	]
	
	def postRequest = { response ->
    
    	try{
        	data.nodes = response.data
        } catch(e){
        	log.error e
        }
		success.call(response)
	}

	
		if (type == 'post') {
			httpPostJson(params, postRequest)
		} else if (type == 'put') {
			httpPutJson(params, postRequest)
		} else if (type == 'get') {
			httpGet(params, postRequest)
		}
	
}

def login(method = null, args = [], success = {}) {
	
	log.debug "Calling login()"
	def params = [
		uri: 'https://api.hivehome.com/v5/login',
		headers:[
			'Content-Type' : 'application/x-www-form-urlencoded;charset=UTF-8'
		],
		body: 'username=' + settings.username + '&password=' + settings.password
	]

	state.cookie = ''
	
	try{
		
		httpPostJson(params) {response ->
			log.debug "Request was successful, $response.status"
			data.auth = response.data
			
			state.cookie = "ApiSession=" + data.auth.ApiSession + "; " + response?.headers?.'Set-Cookie'?.split(";")?.getAt(0)
			
			// set the expiration to 5 minutes
			data.auth.expires_at = new Date().getTime() + 300000;
			
			log.debug "cookie: $state.cookie"
			log.debug "auth: $data.auth"
			
			api(method, args, success)

	}
		
	} catch(e) {
		log.debug "something went wrong: $e"
	}
	
}

def isLoggedIn() {

	log.debug "Calling isLoggedIn()"
	log.debug "isLoggedIn state $data.auth"
    log.debug "Cookie: $state.cookie"
	if(!data.auth) {
		log.debug "No data.auth"
		return false
	}

	def now = new Date().getTime();
	return data.auth.expires_at > now
}
