var categories = new Array()

function getCategoryId(category)
{
    switch (category) {
    case "rusulaceae":
        return "Сы";
    case "lactarius":
        return "Мл";
    case "agaricales":
        return "Пл";
    case "boletales":
        return "Тр";
    case "cortinarius":
        return "Па";
    case "hygrophorus":
        return "Ги";
    case "others":
        return "Ос";
    case "poisonous":
        return "Яд";
    case "fallovye":
        return "Фа";
    case "triholomovye":
        return "Тр";
    case "syroejkovye":
        return "Сы";
    case "strofarievye":
        return "Ст";
    case "sarkoscifovye":
        return "Са";
    case "plevrotovye":
        return "Пл";
    case "pironemovye":
        return "Пи";
    case "navoznikovye":
        return "На";
    case "likoperdovye":
        return "Ли";
    case "muhomorovye":
        return "Му";
    case "lepiotovye":
        return "Ле";
    case "krepidotovye":
        return "Кр";
    case "kortinarievye":
        return "Ко";
    case "ejovikovye":
        return "Еж";
    case "gigroforovye":
        return "Ги";
    case "gelvellovye":
        return "Ге";
    case "agrikovye":
        return "Аг";
    case "boletovye":
        return "Бо";
    default:
        return "";
    }
}

function getCategoryName(category)
{
    switch (category) {
    case "rusulaceae":
        return "Сыроежковые";
    case "lactarius":
        return "Млечники";
    case "agaricales":
        return "Пластинчатые";
    case "boletales":
        return "Трубчатые";
    case "cortinarius":
        return "Паутинники";
    case "hygrophorus":
        return "Гигрофы";
    case "others":
        return "Остальные грибы";
    case "poisonous":
        return "Ядовитые";
    case "fallovye":
        return "Фалловые";
    case "triholomovye":
        return "Трихоломовые";
    case "syroejkovye":
        return "Сыроежковые";
    case "strofarievye":
        return "Строфариевые";
    case "sarkoscifovye":
        return "Саркосцифовые";
    case "plevrotovye":
        return "Плевротовые";
    case "pironemovye":
        return "Пиронемовые";
    case "navoznikovye":
        return "Навозниковые";
    case "likoperdovye":
        return "Ликопердовые";
    case "muhomorovye":
        return "Мухоморовые";
    case "lepiotovye":
        return "Лепиотовые";
    case "krepidotovye":
        return "Крепидотовые";
    case "kortinarievye":
        return "Кортинариевые";
    case "ejovikovye":
        return "Ежовиковые";
    case "gigroforovye":
        return "Гигрофоровые";
    case "gelvellovye":
        return "Гельвелловые";
    case "agrikovye":
        return "Агариковые";
    case "boletovye":
        return "Болетовые";
    default:
        return category;
    }
}
