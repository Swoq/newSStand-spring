$('.addPeriodBtn').on('click', addPeriod);
$('.addGenreBtn').on('click', addGenre);

function addPeriod() {
    $('.addPeriodForm').attr("type", "text");
    $('#submitPeriodBtn').attr("type", "submit");
    $('#days').attr("type", "number");
}

function addGenre() {
    $('.addGenreForm').attr("type", "text");
    $('#submitGenreBtn').attr("type", "submit");
}