$(document).ready(function () {
    $('#genreMultiSelect').bsMultiSelect();
});

$('.custom-file-input').on('change', function() {
    let fileName = $(this).val().split('\\').pop();
    $(this).next('.custom-file-label').addClass("selected").html(fileName);
});

//Clone the hidden element and shows it
$('.add-one').click(function(){
    $('.dynamic-element').first().clone().appendTo('.dynamic-stuff').show();
    attach_delete();
});

//Attach functionality to delete buttons
function attach_delete(){
    $('.delete').off();
    $('.delete').click(function(){
        console.log("click");
        $(this).closest('.form-group').remove();
    });
}