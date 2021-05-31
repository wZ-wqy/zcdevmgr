$("#flagcb").click(function () {
    if ($("#leftcb").hasClass("FnqzS")) {
        $("#leftcb").removeClass("FnqzS");
        $("#leftcb").addClass("gByVX");
    } else {
        $("#leftcb").removeClass("gByVX");
        $("#leftcb").addClass("FnqzS");
    }
    if ($("#flagcb2").hasClass("fa-angle-double-right")) {
        $("#flagcb2").removeClass("fa-angle-double-right");
        $("#flagcb2").addClass("fa-angle-double-left");
    } else {
        $("#flagcb2").removeClass("fa-angle-double-left");
        $("#flagcb2").addClass("fa-angle-double-right");
    }
})
