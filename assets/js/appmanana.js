        var timeout = 240000;
        var timer;
        (function ($) {
            "use strict";
            $.extend({
                simpleWeather: function (options) {
                    options = $.extend({
                        zipcode: '',
                        woeid: '2357536',
                        location: '',
                        unit: 'f',
                        success: function (weather) { },
                        error: function (message) { }
                    }, options);

                    var now = new Date();

                    var weatherUrl = 'http://query.yahooapis.com/v1/public/yql?format=json&rnd=' + now.getFullYear() + now.getMonth() + now.getDay() + now.getHours() + '&diagnostics=true&callback=?&diagnostics=true&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&q=';
                    if (options.location !== '') {
                        weatherUrl += 'select * from weather.forecast where location in (select id from weather.search where query="' + options.location + '") and u="' + options.unit + '"';
                    } else if (options.zipcode !== '') {
                        weatherUrl += 'select * from weather.forecast where location in ("' + options.zipcode + '") and u="' + options.unit + '"';
                    } else if (options.woeid !== '') {
                        weatherUrl += 'select * from weather.forecast where woeid=' + options.woeid + ' and u="' + options.unit + '"';
                    } else {
                        options.error("Could not retrieve weather due to an invalid WOEID or location.");
                        return false;
                    }

                    $.getJSON(
                        weatherUrl,
                        function (data) {
                            if (data !== null && data.query.results !== null) {
                                $.each(data.query.results, function (i, result) {
                                    if (result.constructor.toString().indexOf("Array") !== -1) {
                                        result = result[0];
                                    }

                                    var currentDate = new Date();
                                    var sunRise = new Date(currentDate.toDateString() + ' ' + result.astronomy.sunrise);
                                    var sunSet = new Date(currentDate.toDateString() + ' ' + result.astronomy.sunset);

                                    if (currentDate > sunRise && currentDate < sunSet) {
                                        var timeOfDay = 'd';
                                    } else {
                                        var timeOfDay = 'n';
                                    }

                                    var compass = ['N', 'NNE', 'NE', 'ENE', 'E', 'ESE', 'SE', 'SSE', 'S', 'SSW', 'SW', 'WSW', 'W', 'WNW', 'NW', 'NNW', 'N'];
                                    var windDirection = compass[Math.round(result.wind.direction / 22.5)];

                                    if (result.item.condition.temp < 80 && result.atmosphere.humidity < 40) {
                                        var heatIndex = -42.379 + 2.04901523 * result.item.condition.temp + 10.14333127 * result.atmosphere.humidity - 0.22475541 * result.item.condition.temp * result.atmosphere.humidity - 6.83783 * (Math.pow(10, -3)) * (Math.pow(result.item.condition.temp, 2)) - 5.481717 * (Math.pow(10, -2)) * (Math.pow(result.atmosphere.humidity, 2)) + 1.22874 * (Math.pow(10, -3)) * (Math.pow(result.item.condition.temp, 2)) * result.atmosphere.humidity + 8.5282 * (Math.pow(10, -4)) * result.item.condition.temp * (Math.pow(result.atmosphere.humidity, 2)) - 1.99 * (Math.pow(10, -6)) * (Math.pow(result.item.condition.temp, 2)) * (Math.pow(result.atmosphere.humidity, 2));
                                    } else {
                                        var heatIndex = result.item.condition.temp;
                                    }

                                    if (options.unit === "f") {
                                        var unitAlt = "c";
                                        var tempAlt = Math.round((5.0 / 9.0) * (result.item.condition.temp - 32.0));
                                        var highAlt = Math.round((5.0 / 9.0) * (result.item.forecast[0].high - 32.0));
                                        var lowAlt = Math.round((5.0 / 9.0) * (result.item.forecast[0].low - 32.0));
                                        var tomorrowHighAlt = Math.round((5.0 / 9.0) * (result.item.forecast[1].high - 32.0));
                                        var tomorrowLowAlt = Math.round((5.0 / 9.0) * (result.item.forecast[1].low - 32.0));
                                    } else {
                                        var unitAlt = "f";
                                        var tempAlt = Math.round((9.0 / 5.0) * result.item.condition.temp + 32.0);
                                        var highAlt = Math.round((9.0 / 5.0) * result.item.forecast[0].high + 32.0);
                                        var lowAlt = Math.round((9.0 / 5.0) * result.item.forecast[0].low + 32.0);
                                        var tomorrowHighAlt = Math.round((5.0 / 9.0) * (result.item.forecast[1].high + 32.0));
                                        var tomorrowLowAlt = Math.round((5.0 / 9.0) * (result.item.forecast[1].low + 32.0));
                                    }

                                    var weather = {
                                        title: result.item.title,
                                        temp: result.item.condition.temp,
                                        tempAlt: tempAlt,
                                        code: result.item.condition.code,
                                        todayCode: result.item.forecast[0].code,
                                        units: {
                                            temp: result.units.temperature,
                                            distance: result.units.distance,
                                            pressure: result.units.pressure,
                                            speed: result.units.speed,
                                            tempAlt: unitAlt
                                        },
                                        currently: result.item.condition.text,
                                        high: result.item.forecast[0].high,
                                        highAlt: highAlt,
                                        low: result.item.forecast[0].low,
                                        lowAlt: lowAlt,
                                        forecast: result.item.forecast[0].text,
                                        wind: {
                                            chill: result.wind.chill,
                                            direction: windDirection,
                                            speed: result.wind.speed
                                        },
                                        humidity: result.atmosphere.humidity,
                                        heatindex: heatIndex,
                                        pressure: result.atmosphere.pressure,
                                        rising: result.atmosphere.rising,
                                        visibility: result.atmosphere.visibility,
                                        sunrise: result.astronomy.sunrise,
                                        sunset: result.astronomy.sunset,
                                        description: result.item.description,
                                        thumbnail: "http://l.yimg.com/a/i/us/nws/weather/gr/" + result.item.condition.code + timeOfDay + "s.png",
                                        image: "http://l.yimg.com/a/i/us/nws/weather/gr/" + result.item.condition.code + timeOfDay + ".png",
                                        tomorrow: {
                                            high: result.item.forecast[1].high,
                                            highAlt: tomorrowHighAlt,
                                            low: result.item.forecast[1].low,
                                            lowAlt: tomorrowLowAlt,
                                            forecast: result.item.forecast[1].text,
                                            code: result.item.forecast[1].code,
                                            date: result.item.forecast[1].date,
                                            day: result.item.forecast[1].day,
                                            image: "http://l.yimg.com/a/i/us/nws/weather/gr/" + result.item.forecast[1].code + "d.png"
                                        },
                                        city: result.location.city,
                                        country: result.location.country,
                                        region: result.location.region,
                                        updated: result.item.pubDate,
                                        link: result.item.link
                                    };

                                    options.success(weather);
                                });
                            } else {
                                if (data.query.results === null) {
                                    options.error("An invalid WOEID or location was provided.");
                                } else {
                                    options.error("There was an error retrieving the latest weather information. Please try again.");
                                }
                            }
                        }
                    );
                    return this;
                }
            });
            //el timer
            jQuery.timer = function (interval, callback, options) {
                // Create options for the default reset value
                var options = jQuery.extend({ reset: 500 }, options);
                var interval = interval || options.reset;

                if (!callback) { return false; }

                var Timer = function (interval, callback, disabled) {
                    // Only used by internal code to call the callback
                    this.internalCallback = function () { callback(self); };

                    // Clears any timers
                    this.stop = function () { clearInterval(self.id); };
                    // Resets timers to a new time
                    this.reset = function (time) {
                        if (self.id) { clearInterval(self.id); }
                        var time = time || options.reset;

                        this.id = setInterval(this.internalCallback, time);
                    };

                    // Set the interval time again
                    this.interval = interval;

                    // Set the timer, if enabled
                    if (!disabled) {
                        this.id = setInterval(this.internalCallback, this.interval);
                    }

                    var self = this;
                };

                // Create a new timer object
                return new Timer(interval, callback, options.disabled);
            };



            $.simpleWeather({
                //zipcode: '815000',
                //location: 'Santiago',
                woeid: '349859',//santiago
                location: '',
                unit: 'c',
                success: function (weather) {
                    //para hoy
                    var html1 = '';
                    html1 += '<h1 class="manana1">Pronóstico para mañana</h1>';
                    html1 += '<div class="separador"></div>';
                    html1 += '<h1 class="manana">' + transformarPronostico(weather.tomorrow.code) + '</h1>';
                    html1 += '<h1 class="manana">Min/Max: ' + weather.tomorrow.low + ' - ' + weather.tomorrow.high + ' &deg;C</h1>';
                    html1 += '<h1 class="manana2">' + entregaHora()+ '</h1>';
                    html1 += "<hr>";
                    $("#weather1").html(html1);
                    showAndroidToastManana(weather.tomorrow.code);

                },
                error: function (error) {
                    $("#weather").html('<p>' + error + '</p>');
                }
            });

            timer = $.timer(timeout, function () {
                //$("#console").append("Timer completed.<br />");
                //alert("Timer completed");
                entregaHora();

                $.simpleWeather({
                    //zipcode: '815000',
                    //location: 'Santiago',
                    woeid: '349859',//santiago
                    location: '',
                    unit: 'c',
                    success: function (weather) {
                        var html1 = '';
                        html1 += '<h1 class="manana1">Pronóstico para mañana</h1>';
                        html1 += '<div class="separador"></div>';
                        html1 += '<h1 class="manana">' + transformarPronostico(weather.tomorrow.code) + '</h1>';
                        html1 += '<h1 class="manana">Min/Max: ' + weather.tomorrow.low + ' - ' + weather.tomorrow.high + ' &deg;C</h1>';
                        html1 += '<h1 class="manana2">' + entregaHora()+ '</h1>';
                        html1 += "<hr>";
                        $("#weather1").html(html1);
                        showAndroidToastManana(weather.tomorrow.code);

                    },
                    error: function (error) {
                        $("#weather").html('<p>' + error + '</p>');
                    }

                });

            });

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

            function entregaHora() {

                var today = new Date();
                //alert(fecha);

                var h = today.getHours();
                var m = today.getMinutes();
                var s = today.getSeconds();
                m = checkTime(m);
                s = checkTime(s);
                var html1 = "Actualizado: ";
                return html1 + h + ":" + m + ":" + s;
                //t = setTimeout('startTime()', 500);

            }
            function checkTime(i) {
                if (i < 10) { i = "0" + i; } return i;
            }
            function iniciar() {
                var widget = document.getElementById('weather1');
                widget.addEventListener('click', actualizar, false);
            }
            function actualizar() {
                $.simpleWeather({
                    //zipcode: '815000',
                    //location: 'Santiago',
                    woeid: '349859',//santiago
                    location: '',
                    unit: 'c',
                    success: function (weather) {
                        var html1 = '';
                        html1 += '<h1 class="manana1">Pronóstico para mañana</h1>';
                        html1 += '<div class="separador"></div>';
                        html1 += '<h1 class="manana">' + transformarPronostico(weather.tomorrow.code) + '</h1>';
                        html1 += '<h1 class="manana">Min/Max: ' + weather.tomorrow.low + ' - ' + weather.tomorrow.high + ' &deg;C</h1>';
                        html1 += '<h1 class="manana2">' + entregaHora()+ '</h1>';
                        html1 += "<hr>";
                        $("#weather1").html(html1);
                        showAndroidToastManana(weather.tomorrow.code);

                    },
                    error: function (error) {
                        $("#weather").html('<p>' + error + '</p>');
                    }
                });
            }
            function showAndroidToastManana(toast) {
                AndroidFunctionM.showManana(toast);
            }
            window.addEventListener('load', iniciar, false);

        })(jQuery);
        //function showAndroidToast(toast) {
        //    AndroidFunction.showToast(toast);
        //}
        //function showAndroidToastManana(toast) {
        //    AndroidFunction.showManana(toast);
        //}
        //window.addEventListener('load', iniciar, false);

