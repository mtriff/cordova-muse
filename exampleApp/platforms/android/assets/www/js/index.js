var app = {
    // Application Constructor
    initialize: function() {
        this.bindEvents();
    },
    // Bind Event Listeners
    //
    // Bind any events that are required on startup. Common events are:
    // 'load', 'deviceready', 'offline', and 'online'.
    bindEvents: function() {
        document.addEventListener('deviceready', this.onDeviceReady, false);
    },
    // deviceready Event Handler
    //
    // The scope of 'this' is the event. In order to call the 'receivedEvent'
    // function, we must explicitly call 'app.receivedEvent(...);'
    onDeviceReady: function() {
        app.receivedEvent('deviceready');
    },
    // Update DOM on a Received Event
    receivedEvent: function(id) {
        var parentElement = document.getElementById(id);
        var listeningElement = parentElement.querySelector('.listening');
        var receivedElement = parentElement.querySelector('.received');

        listeningElement.setAttribute('style', 'display:none;');
        receivedElement.setAttribute('style', 'display:block;');

        console.log('Received Event: ' + id);
    }
};

function connectMuse() {
    muse.connectToMuse(function(returnMsg) {
        var buttons = document.getElementsByClassName("museConnected");
        for (var i = 0; i < buttons.length; i++) {
            buttons[i].disabled = false;
        }
    });
}

function disconnectMuse() {
    muse.disconnectMuse(function(returnMsg) {
        var buttons = document.getElementsByClassName("museConnected");
        for (var i = 0; i < buttons.length; i++) {
            buttons[i].disabled = true;
        }
    });
}

function startRecording() {
    muse.startRecording();
}

function stopRecording() {
    muse.stopRecording();
}

function getAccForwardBackward() {
    muse.getAccForwardBackward(function(returnMsg) {
        alert(returnMsg);
    }, function(returnMsg) {
        alert(returnMsg);
    });
}

function getAccLeftRight() {
    muse.getAccLeftRight(function(returnMsg) {
        alert(returnMsg);
    }, function(returnMsg) {
        alert(returnMsg);
    });
}

function getAccUpDown() {
    muse.getAccUpDown(function(returnMsg) {
        alert(returnMsg);
    }, function(returnMsg) {
        alert(returnMsg);
    });
}

function getEegLeftEar() {
    muse.getEegLeftEar(function(returnMsg) {
        alert(returnMsg);
    }, function(returnMsg) {
        alert(returnMsg);
    });
}

function getEegRightEar() {
    muse.getEegRightEar(function(returnMsg) {
        alert(returnMsg);
    }, function(returnMsg) {
        alert(returnMsg);
    });
}

function getEegLeftForehead() {
    muse.getEegLeftForehead(function(returnMsg) {
        alert(returnMsg);
    }, function(returnMsg) {
        alert(returnMsg);
    });
}

function getEegRightForehead() {
    muse.getEegRightForehead(function(returnMsg) {
        alert(returnMsg);
    }, function(returnMsg) {
        alert(returnMsg);
    });
}

function getAlphaRelativeChannel1() {
    muse.getAlphaRelativeChannel1(function(returnMsg) {
        alert(returnMsg);
    }, function(returnMsg) {
        alert(returnMsg);
    });
}

function getAlphaRelativeChannel2() {
    muse.getAlphaRelativeChannel2(function(returnMsg) {
        alert(returnMsg);
    }, function(returnMsg) {
        alert(returnMsg);
    });
}

function getAlphaRelativeChannel3() {
    muse.getAlphaRelativeChannel3(function(returnMsg) {
        alert(returnMsg);
    }, function(returnMsg) {
        alert(returnMsg);
    });
}

function getAlphaRelativeChannel4() {
    muse.getAlphaRelativeChannel4(function(returnMsg) {
        alert(returnMsg);
    }, function(returnMsg) {
        alert(returnMsg);
    });
}

function getBetaRelativeChannel1() {
    muse.getBetaRelativeChannel1(function(returnMsg) {
        alert(returnMsg);
    }, function(returnMsg) {
        alert(returnMsg);
    });
}

function getBetaRelativeChannel2() {
    muse.getBetaRelativeChannel2(function(returnMsg) {
        alert(returnMsg);
    }, function(returnMsg) {
        alert(returnMsg);
    });
}

function getBetaRelativeChannel3() {
    muse.getBetaRelativeChannel3(function(returnMsg) {
        alert(returnMsg);
    }, function(returnMsg) {
        alert(returnMsg);
    });
}

function getBetaRelativeChannel4() {
    muse.getBetaRelativeChannel4(function(returnMsg) {
        alert(returnMsg);
    }, function(returnMsg) {
        alert(returnMsg);
    });
}

function getThetaRelativeChannel1() {
    muse.getThetaRelativeChannel1(function(returnMsg) {
        alert(returnMsg);
    }, function(returnMsg) {
        alert(returnMsg);
    });
}

function getThetaRelativeChannel2() {
    muse.getThetaRelativeChannel2(function(returnMsg) {
        alert(returnMsg);
    }, function(returnMsg) {
        alert(returnMsg);
    });
}

function getThetaRelativeChannel3() {
    muse.getThetaRelativeChannel3(function(returnMsg) {
        alert(returnMsg);
    }, function(returnMsg) {
        alert(returnMsg);
    });
}

function getThetaRelativeChannel4() {
    muse.getThetaRelativeChannel4(function(returnMsg) {
        alert(returnMsg);
    }, function(returnMsg) {
        alert(returnMsg);
    });
}

function getGammaRelativeChannel1() {
    muse.getGammaRelativeChannel1(function(returnMsg) {
        alert(returnMsg);
    }, function(returnMsg) {
        alert(returnMsg);
    });
}

function getGammaRelativeChannel2() {
    muse.getGammaRelativeChannel2(function(returnMsg) {
        alert(returnMsg);
    }, function(returnMsg) {
        alert(returnMsg);
    });
}

function getGammaRelativeChannel3() {
    muse.getGammaRelativeChannel3(function(returnMsg) {
        alert(returnMsg);
    }, function(returnMsg) {
        alert(returnMsg);
    });
}

function getGammaRelativeChannel4() {
    muse.getGammaRelativeChannel4(function(returnMsg) {
        alert(returnMsg);
    }, function(returnMsg) {
        alert(returnMsg);
    });
}

function getBlink() {
    muse.getBlink(function(returnMsg) {
        alert(returnMsg);
    }, function(returnMsg) {
        alert(returnMsg);
    });
}

function getRecordingData() {
    muse.getRecordingData(function(returnMsg) {
        alert(JSON.stringify(returnMsg));
    }, function(returnMsg) {
        alert(returnMsg);
    });
}

function testConnection() {
    muse.testConnection(function(returnMsg) {
        // alert(returnMsg);
    }, function(returnMsg) {
        alert(returnMsg);
    });
}


app.initialize();