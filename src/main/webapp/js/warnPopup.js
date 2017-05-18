/**
 * Created by Peter on 14-2-21.
 */
    //warn popup
function warnPopup(warnText){
    $(".warn_popup .warn_msg").text(warnText);
    $(".warn_popup").fadeIn("noraml").delay(2000).fadeOut("normal");
}