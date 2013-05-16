var bindCss = ".record-flow";
var endPoint = "http://http://flow-gassies.rhcloud.com/flow";
var session = "nosession";

$(document).ready(function() {

    $.ajax({
        type: "GET",
        url: endPoint + "/getsession",
        success: function(data) {
           if (console) console.log("session: " + data)
           session = data;
        },
        error: function(response, status, error) {
            alert(status+": "+error);
        }
    });

    function storeSession(data) {
    }

    $(bindCss+"-click")     .click(     function(eventObject) {handleGenericEvent(      "click",      eventObject);});
    $(bindCss+"-blur")      .blur(      function(eventObject) {handleGenericEvent(      "blur",       eventObject);});
    $(bindCss+"-dblclick")  .dblclick(  function(eventObject) {handleGenericEvent(      "dblclick",   eventObject);});
    $(bindCss+"-mouseover") .mouseover( function(eventObject) {handleAggregatableEvent( "mouseover",  eventObject);});
    $(bindCss+"-mouseout")  .mouseout(  function(eventObject) {handleGenericEvent(      "mouseout",   eventObject);});
    $(bindCss+"-mousedown") .mousedown( function(eventObject) {handleGenericEvent(      "mousedown",  eventObject);});
    $(bindCss+"-mouseup")   .mouseup(   function(eventObject) {handleGenericEvent(      "mouseup",    eventObject);});
    $(bindCss+"-mousemove") .mousemove( function(eventObject) {handleAggregatableEvent( "mousemove",  eventObject);});
    $(bindCss+"-keypress")  .keypress(  function(eventObject) {handleGenericEvent(      "keypress",   eventObject);});
    $(bindCss+"-keydown")   .keydown(   function(eventObject) {handleGenericEvent(      "keydown",    eventObject);});
    $(bindCss+"-keyup")     .keyup(     function(eventObject) {handleGenericEvent(      "keyup",      eventObject);});
    $(bindCss+"-submit")    .submit(    function(eventObject) {handleGenericEvent(      "submit",     eventObject);});
    $(bindCss+"-focus")     .focus(     function(eventObject) {handleGenericEvent(      "focus",      eventObject);});
    $(bindCss+"-blur")      .blur(      function(eventObject) {handleGenericEvent(      "blur",       eventObject);});
    $(bindCss+"-load")      .load(      function(eventObject) {handleGenericEvent(      "load",       eventObject);});
    $(bindCss+"-unload")    .unload(    function(eventObject) {handleGenericEvent(      "unload",     eventObject);});
    $(bindCss+"-resize")    .resize(    function(eventObject) {handleGenericEvent(      "resize",     eventObject);});
    $(bindCss+"-scroll")    .scroll(    function(eventObject) {handleGenericEvent(      "scroll",     eventObject);});


    function handleGenericEvent(eventName, eventObject) {
        var data = {
            url:                document.location.href,
            useragent:          navigator.userAgent,
            element:            eventObject.target.nodeName,
            value:              eventObject.target.value,
            text:               eventObject.textContent,
            key:                eventObject.charCode || eventObject.keyCode,
            link:               _getLink(eventObject)
        }

        $.each(eventObject.currentTarget.attributes, function(i) {
            var attribute = eventObject.currentTarget.attributes[i];
            if (attribute.name.indexOf("data-")== 0) {
                data[attribute.name] = attribute.value;
            }
        });

        $.ajax({
          type: "POST",
          url: endPoint + "/event/" + session + "/" + eventName,
          data: data,
          success: function(data) {
            if (console) console.log(data);
          },
          error: function(response, status, error) {
            alert(status+": "+error);
          }
        });

        function success(data) {
            if (console) console.log(data);
        }
    }

    function _getLink(eventObject) {
        if (eventObject.currentTarget.getAttribute("src")) {
            return eventObject.currentTarget.getAttribute("src");
        }
        if (eventObject.currentTarget.getAttribute("href")) {
            return eventObject.currentTarget.getAttribute("href");
        }
        return "";
    }

    function handleAggregatableEvent(eventName, eventObject) {
        if (console) console.log("not yet implemented");
    }
});

