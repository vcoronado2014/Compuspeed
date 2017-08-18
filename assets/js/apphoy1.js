function transformarPronostico(currently) {
    //alert(currently);
    var ret = '';
    switch (currently) {
        case '0':
            ret = 'Tornado';
            break;

        case '1':
            ret = 'Tormenta tropical';
            break;

        case '2':
            ret = 'Huracán';
            break;

        case '3':
            ret = 'Tormentas severas'
            break;

        case '4':
            ret = 'Tormentas';
            break;

        case '5':
            ret = 'Lluvia y nieve mezcladas';
            break;

        case '6':
            ret = 'Lluvia mezclada y aguanieve';
            break;

        case '7':
            ret = 'Nieve y aguanieve mixta';
            break;

        case '8':
            ret = 'Llovizna helada';
            break;

        case '9':
            ret = 'Llovizna';
            break;

        case '10':
            ret = 'Lluvia helada';
            break;

        case '11':
            ret = 'Lluvia intensa';
            break;

        case '12':
            ret = 'Lluvia intensa';
            break;

        case '13':
            ret = 'Copos de nieve';
            break;

        case '14':
            ret = 'Nevadas ligeras';
            break;

        case '15':
            ret = 'Viento y nieve';
            break;

        case '16':
            ret = 'Nieve';
            break;

        case '17':
            ret = 'Granizo';
            break;

        case '18':
            ret = 'Aguanieve';
            break;

        case '19':
            ret = 'Polvo';
            break;

        case '20':
            ret = 'Brumoso';
            break;

        case '21':
            ret = 'Neblina';
            break;

        case '22':
            ret = 'Ahumado';
            break;

        case '23':
            ret = 'Borrascoso';
            break;

        case '24':
            ret = 'Ventoso';
            break;

        case '25':
            ret = 'Frío';
            break;

        case '26':
            ret = 'Nublado';
            break;

        case '27':
            ret = 'Parcialmente nublado (noche)';
            break;

        case '28':
            ret = 'Parcialmente nublado (día)';
            break;

        case '29':
            ret = 'Parcialmente nublado (noche)';
            break;

        case '30':
            ret = 'Parcialmente nublado (día)';
            break;

        case '31':
            ret = 'Borrar (noche)';
            break;

        case '32':
            ret = 'Soleado';
            break;

        case '33':
            ret = 'Despejado (noche)';
            break;

        case '34':
            ret = 'Despejado (días)';
            break;

        case '35':
            ret = 'La lluvia y el granizo mezclado';
            break;

        case '36':
            ret = 'Caluroso';
            break;

        case '37':
            ret = 'Tormentas aisladas';
            break;

        case '38':
            ret = 'Tormentas eléctricas dispersas';
            break;

        case '39':
            ret = 'Tormentas eléctricas dispersas';
            break;

        case '40':
            ret = 'Aguaceros dispersos';
            break;

        case '41':
            ret = 'Mucha nieve';
            break;

        case '42':
            ret = 'Chubascos de nieve';
            break;

        case '43':
            ret = 'Mucha nieve';
            break;

        case '44':
            ret = 'Parcialmente nublado';
            break;

        case '45':
            ret = 'Tormentosos';
            break;

        case '46':
            ret = 'Nieve';
            break;

        case '47':
            ret = 'Chubascos aislados';
            break;

        case '3200':
            ret = 'No disponible';
            break;

        default:
            ret = 'No disponible';
            break;
    }
    //alert(ret);
    return ret;
}
function showAndroidToast(toast) {
    AndroidFunction.showToast(toast);
}