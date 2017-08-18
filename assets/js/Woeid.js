

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
                            //_getWeatherAddress(data.query.results.place[i]);
                            //data.query.results.place[i].woeid
                            //alert('else');
                            $('#test').weatherfeed([data.query.results.place[i].woeid], {
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
                        else {
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
                    }
                } else {
                    //alert('else');
                    $('#test').weatherfeed([data.query.results.place.woeid], {
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
                    //_getWeatherAddress(data.query.results.place);
                }

            } else {
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

function _getWeatherAddress(data) {

    // Get address
    var address = data.name;
    if (data.admin2) address += ', ' + data.admin2.content;
    if (data.admin1) address += ', ' + data.admin1.content;
    address += ', ' + data.country.content;

    // Get WEOID
    var woeid = data.woeid;
    alert(woeid);
    return (data.woeid);


}
function _getWeatherAddressDefecto(defecto) {

    return defecto;

}
