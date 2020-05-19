/*事件处理对象*/
var EventUtil = {

    /*添加事件监听*/
    add: function(element, type, callback) {

        if (element.addEventListener) {
            element.addEventListener(type, callback, false);
        } else if (element.attachEvent) {
            element.attachEvent('on' + type, callback);
        } else {
            element['on' + type] = callback;
        }
    },

    /*移除事件监听*/
    remove: function(element, type, callback) {

        if (element.removeEventListener) {
            element.removeEventListener(type, callback, false);
        } else if (element.detachEvent) {
            element.detachEvent('on' + type, callback);
        } else {
            element['on' + type] = null;
        }

    },

    /*跨浏览器获取 event 对象*/
    getEvent: function(event) {

        return event ? event: window.event;
    },

    /*跨浏览器获取 target 属性*/
    getTarget: function(event) {

        return event.target || event.srcElement;
    },

    /*阻止事件的默认行为*/
    preventDefault: function(event) {

        if (event.preventDefault) {
            event.preventDefault();
        } else {
            event.returnValue = false;
        }
    },

    /*阻止事件流或使用 cancelBubble*/
    stopPropagation: function() {

        if (event.stopPropagation) {
            event.stopPropagation();
        } else {
            event.cancelBubble = true;
        }
    }
};

var at = $('.player-icon icon-widescreen');

alert('haha');

EventUtil.add(at, 'click',function() {
    if (isFullScreen()) {
        exitFullScreen();
    } else {
        var video = document.getElementsByClassName('display')[0];
        full(video);
    }
    /*if(isFullScreen()){
        window.bilibili.exitFullScreen();
    }else{
        window.bilibili.fullScreen();
    }*//*
});*/

/*判断是否全屏*/
function isFullScreen() {
    return !! (document.fullscreen || document.mozFullScreen || document.webkitIsFullScreen || document.webkitFullScreen || document.msFullScreen);
}

/*退出全屏*/
function exitFullscreen() {
    if (document.exitFullScreen) {
        document.exitFullScreen();
    } else if (document.mozCancelFullScreen) {
        document.mozCancelFullScreen();
    } else if (document.webkitExitFullscreen) {
        document.webkitExitFullscreen();
    } else if (document.msExitFullscreen) {
        document.msExitFullscreen();
    }
}

/*判断是否能全屏*/
function isFullscreenEnabled() {
    return (document.fullscreenEnabled || document.mozFullScreenEnabled || document.webkitFullscreenEnabled || document.msFullscreenEnabled);
}

/*全屏*/
function full(ele) {
    ele.click();
    /*first the standard version*/
    if (ele.requestFullscreen) {
        ele.requestFullscreen();
    } else if (ele.webkitRequestFullscreen) {
        ele.webkitRequestFullscreen();
    } else if (ele.mozRequestFullScreen) {
        ele.mozRequestFullScreen();
    } else if (ele.msRequestFullscreen) {
        ele.msRequestFullscreen();
    }
}