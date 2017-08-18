function weatherGeocode(search, output, pais) {
    var defecto = '349859';
    var status;
    var results;
    var html = '';
    var msg = '';
    //alert(search);
    // Set document elements
    var search1 = search;



    // Cache results for an hour to prevent overuse
    now = new Date();

    // Create Yahoo Weather feed API address
    var query = 'select * from geo.places where text="' + search + '"';
    var api = 'http://query.yahooapis.com/v1/public/yql?q=' + encodeURIComponent(query) + '&rnd=' + now.getFullYear() + now.getMonth() + now.getDay() + now.getHours() + '&format=json&callback=?';

    // Send request
    $.ajax({
        type: 'GET',
        url: api,
        dataType: 'json',
        async: false,
        success: function (data) {
            //alert('else');
            if (data.query.count > 0) {
                // List multiple returns
                //alert('else');
                if (data.query.count > 1) {
                    for (var i = 0; i < data.query.count; i++) {
                        if (pais == data.query.results.place[i].country.content) {
                            Obtener(data.query.results.place[i].woeid);
                            return;
                        }
                        else {
                            Obtener(defecto);
                            return;
                        }
                    }
                } else {
                    //alert('else');
                    Obtener(data.query.results.place.woeid);
                    return;
                    //_getWeatherAddress(data.query.results.place);
                }

            } else {
                Obtener(defecto);
                return;
                // _getWeatherAddressDefecto(defecto);
            }
        },
        error: function (data) {
            //_getWeatherAddressDefecto(defecto);
            //alert('else');
            $('#test').weatherfeed([defecto], {
                woeid: true,
                unit: 'c',
                image: true,
                country: true,
                highlow: true,
                wind: true,
                humidity: true,
                visibility: true,
                sunrise: true,
                sunset: true,
                forecast: true,
                link: false
            });
            return;
        }
    });

    //alert('else');
}
function Obtener(woeidBuscar) {

    var unit = 'c';
    now = new Date();

    // Select location ID type
    var queryType = true ? 'woeid' : 'location';

    // Create Yahoo Weather feed API address
    var query = "select * from weather.forecast where " + queryType + " in (" + woeidBuscar + ") and u='" + unit + "'";
    var api = 'http://query.yahooapis.com/v1/public/yql?q=' + encodeURIComponent(query) + '&rnd=' + now.getFullYear() + now.getMonth() + now.getDay() + now.getHours() + '&format=json&callback=?';
    
    // Send request
    $.ajax({
        type: 'GET',
        url: api,
        dataType: 'json',
        success: function (data) {

            if (data.query) {

                if (data.query.results.channel.length > 0) {

                    // Multiple locations
                    var result = data.query.results.channel.length;
                    for (var i = 0; i < result; i++) {

                        // Create weather feed item
                        //_process(e, data.query.results.channel[i], options);
                        //alert(data.query.results.channel[i]);
                        MostrarLosDatos(data.query.results.channel[i]);
                    }
                } else {

                    // Single location only
                    //_process(e, data.query.results.channel, options);
                    //alert(data.query.results.channel);
                    MostrarLosDatos(data.query.results.channel);

                }

                // Optional user callback function
                //if ($.isFunction(fn)) fn.call(this, $e);

            } else {
                alert('No habilitada');
                //if (options.showerror) $e.html('<p>Weather information unavailable</p>');
            }
        },
        error: function (data) {
            //if (options.showerror) $e.html('<p>Weather request failed</p>');
            alert('sin respuesta');
        }
    });


}
function MostrarLosDatos(feed) {
    var contenedor = $('#test');
    contenedor.empty();
    var row = 'odd';

    if (feed.description != 'Yahoo! Weather Error') {
        var wd = feed.wind.direction;
        if (wd >= 348.75 && wd <= 360) { wd = "N" }; if (wd >= 0 && wd < 11.25) { wd = "N" }; if (wd >= 11.25 && wd < 33.75) { wd = "NNE" }; if (wd >= 33.75 && wd < 56.25) { wd = "NE" }; if (wd >= 56.25 && wd < 78.75) { wd = "ENE" }; if (wd >= 78.75 && wd < 101.25) { wd = "E" }; if (wd >= 101.25 && wd < 123.75) { wd = "ESE" }; if (wd >= 123.75 && wd < 146.25) { wd = "SE" }; if (wd >= 146.25 && wd < 168.75) { wd = "SSE" }; if (wd >= 168.75 && wd < 191.25) { wd = "S" }; if (wd >= 191.25 && wd < 213.75) { wd = "SSW" }; if (wd >= 213.75 && wd < 236.25) { wd = "SW" }; if (wd >= 236.25 && wd < 258.75) { wd = "WSW" }; if (wd >= 258.75 && wd < 281.25) { wd = "W" }; if (wd >= 281.25 && wd < 303.75) { wd = "WNW" }; if (wd >= 303.75 && wd < 326.25) { wd = "NW" }; if (wd >= 326.25 && wd < 348.75) { wd = "NNW" };
        var wf = feed.item.forecast[0];

        // Determine day or night image
        wpd = feed.item.pubDate;
        n = wpd.indexOf(":");
        tpb = _getTimeAsDate(wpd.substr(n - 2, 8));
        tsr = _getTimeAsDate(feed.astronomy.sunrise);
        tss = _getTimeAsDate(feed.astronomy.sunset);

        // Get night or day
        if (tpb > tsr && tpb < tss) { daynight = 'day'; } else { daynight = 'night'; }
        // Add item container
        var html = '<div class="weatherItem ' + row + ' ' + daynight + '"';
        if (true) html += ' style="background-image: url(img/' + feed.item.condition.code + daynight.substring(0, 1) + '.png); background-repeat: no-repeat; background-position:left top;"';
        html += '>';
		//html += '<img alt="" src="img/' +  feed.item.condition.code + daynight.substring(0, 1) + '.png"/>';
        // Add item data
        html += '<div class="weatherCity">' + feed.location.city + '</div>';
        if (true) html += '<div class="weatherCountry">' + feed.location.country + '</div>';
        html += '<div class="weatherTemp">' + feed.item.condition.temp + '&deg;</div>';
        html += '<div class="weatherDesc">' + transformarPronostico(feed.item.condition.code) + '</div>';

        // Add optional data
        if (true) html += '<div class="weatherRange">Max: ' + wf.high + '&deg; Min: ' + wf.low + '&deg;</div>';
        if (true) html += '<div class="weatherWind">Viento: ' + wd + ' ' + feed.wind.speed + feed.units.speed + '</div>';
        if (true) html += '<div class="weatherHumidity">Humedad: ' + feed.atmosphere.humidity + '%</div>';
        if (true) html += '<div class="weatherVisibility">Visibilidad: ' + feed.atmosphere.visibility + '%</div>';
        if (true) html += '<div class="weatherSunrise">Amanacer: ' + feed.astronomy.sunrise + '</div>';
        if (true) html += '<div class="weatherSunset">Anochecer: ' + feed.astronomy.sunset + '</div>';

        if (true) {
            //html += '<hr>'
            html += '<div class="weatherForecast">';

            var wfi = feed.item.forecast;

            //for (var i = 0; i < wfi.length; i++) {
            for (var i = 0; i < 3; i++) {

                html += '<div class="weatherForecastItem" style="background-image: url(imgs/' + wfi[i].code + daynight.substring(0, 1) + 's.png); background-repeat: no-repeat;">';
                //html += '<div class="weatherForecastItem">';
                html += '<div class="weatherForecastDay">' + transformaDiaCorto(wfi[i].day) + '</div>';
                //html += '<div class="weatherForecastRange">' + wfi[i].low + ' | ' + wfi[i].high + '</div>';
                html += '<div class="weatherForecastRange">' + '<div class="min">' + wfi[i].low + '</div><div class="max">' + wfi[i].high + '</div>' + '</div>';

                html += '</div>'
                //html += '<div class="weatherForecastItem2" style="background-image: url(imgs/' + wfi[i].code + daynight.substring(0, 1) + 's.png); background-repeat: no-repeat;"></div>';
            }

            html += '</div>'
        }

        if (false) html += '<div class="weatherLink"><a href="' + feed.link + '" target="' + options.linktarget + '" title="Read full forecast">Full forecast</a></div>';


    }
    else {
        var html = '<div class="weatherItem ' + row + '">';
        //html += '<div class="weatherError">City not found</div>';
        // Add item data
        html += '<div class="weatherCity">' + 'Desconocido' + '</div>';
        html += '<div class="weatherCountry">' + 'Chile' + '</div>';
        html += '<div class="weatherTemp">' + '--' + '&deg;</div>';
        html += '<div class="weatherDesc">' + 'Sin Red' + '</div>';

        // Add optional data
        html += '<div class="weatherRange">Max: ' + '--' + '&deg; Min: ' + '--' + '&deg;</div>';
        html += '<div class="weatherWind">Viento: ' + '0' + ' ' + '0' + '0' + '</div>';
        html += '<div class="weatherHumidity">Humedad: ' + '0' + '</div>';
        html += '<div class="weatherVisibility">Visibilidad: ' + '0' + '</div>';
        html += '<div class="weatherSunrise">Amanacer: ' + '08:00' + '</div>';
        html += '<div class="weatherSunset">Anochecer: ' + '19:00' + '</div>';
        toast = '32';
        html += '<div class="weatherForecastItem">';
        html += '<hr>'
        html += '<div class="weatherForecast">';

        //for (var i = 0; i < 3; i++) {
        //    html += '<div class="weatherForecastDay">' + '--' + '</div>';
        //    html += '<div class="weatherForecastRange">' + '0' + ' | ' + '0' + '</div>';
        //    html += '</div>'
        //}
        html += '</div>'
    }

    html += '</div>';

    // Alternate row classes
    if (row == 'odd') { row = 'even'; } else { row = 'odd'; }

    alert(html);
    contenedor.append(html);
    // Get time string as date


}

var _getTimeAsDate = function (t) {

    d = new Date();
    r = new Date(d.toDateString() + ' ' + t);

    return r;
};
function transformaDia(day) {
    if (day == 'Mon')
        return 'Lunes';
    if (day == 'Tue')
        return 'Martes';
    if (day == 'Wed')
        return 'Mi&eacute;rcoles';
    if (day == 'Thu')
        return 'Jueves';
    if (day == 'Fri')
        return 'Viernes';
    if (day == 'Sat')
        return 'S&aacute;bado';
    if (day == 'Sun')
        return 'Domingo';
}
function transformaDiaCorto(day) {
    if (day == 'Mon')
        return 'Lun';
    if (day == 'Tue')
        return 'Mar';
    if (day == 'Wed')
        return 'Mi&eacute;';
    if (day == 'Thu')
        return 'Jue';
    if (day == 'Fri')
        return 'Vie';
    if (day == 'Sat')
        return 'S&aacute;b';
    if (day == 'Sun')
        return 'Dom';
}
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
            ret = 'Hurac&aacute;n';
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
            ret = 'Frio';
            break;

        case '26':
            ret = 'Nublado';
            break;

        case '27':
            ret = 'Parcialmente nublado (noche)';
            break;

        case '28':
            ret = 'Parcialmente nublado (d&iacute;a)';
            break;

        case '29':
            ret = 'Parcialmente nublado (noche)';
            break;

        case '30':
            ret = 'Parcialmente nublado (d&iacute;a)';
            break;

        case '31':
            ret = 'Borrascoso (noche)';
            break;

        case '32':
            ret = 'Soleado';
            break;

        case '33':
            ret = 'Despejado (noche)';
            break;

        case '34':
            ret = 'Despejado (d&iacute;s)';
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
            ret = 'Tormentas el&eacute;ctricas dispersas';
            break;

        case '39':
            ret = 'Tormentas el&eacute;ctricas dispersas';
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

