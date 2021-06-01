$(document).ready(function () {
    $('#genreMultiSelect').bsMultiSelect();
});

$('#sortBy').on('change', function (e) {
    let optionSelected = $("option:selected", this);
    let valueSelected = this.value;
    let dateBlock = $('#hiddenDate');
    if (valueSelected === 'date_before' || valueSelected === 'date_after') {
        dateBlock.removeAttr("disabled")
    } else {
        dateBlock.attr("disabled", "disabled");
    }

});