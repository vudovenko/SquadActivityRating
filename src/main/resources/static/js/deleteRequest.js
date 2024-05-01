$(document).ready(function() {
    $('a[data-id]').click(function(e) {
        e.preventDefault(); // Предотвращаем стандартное действие ссылки
        var id = $(this).data('id'); // Получаем ID отряда из атрибута data-id
        var url = '/squads/' + id; // Формируем URL для запроса

        $.ajax({
            type: 'DELETE',
            url: url,
            success: function(result) {
                console.log(result);
                // Здесь можно добавить код для обновления страницы или удаления строки из таблицы
            },
            error: function(e) {
                console.log(e);
            }
        });
    });
});