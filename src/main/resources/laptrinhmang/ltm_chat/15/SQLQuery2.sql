CREATE TABLE HourlyWeather (
    Id INT IDENTITY PRIMARY KEY,
	provinceCode nvarchar(20),
	dateTime DATETIME NOT NULL, -- Thời gian ghi nhận dữ liệu
    RelativeHumidity2m FLOAT, -- Độ ẩm tương đối tại 2m
    DewPoint2m FLOAT, -- Điểm sương tại 2m
    ApparentTemperature FLOAT, -- Nhiệt độ cảm nhận
    PrecipitationProbability FLOAT, -- Xác suất mưa
    Precipitation FLOAT, -- Lượng mưa
    Rain FLOAT, -- Lượng mưa thực tế
    WeatherCode INT, -- Mã thời tiết
    PressureMSL FLOAT, -- Áp suất mực nước biển
    SurfacePressure FLOAT, -- Áp suất bề mặt
    CloudCover FLOAT, -- Tỷ lệ mây bao phủ
    CloudCoverLow FLOAT, -- Mây thấp
    CloudCoverMid FLOAT, -- Mây trung bình
    CloudCoverHigh FLOAT, -- Mây cao
    Visibility FLOAT, -- Tầm nhìn
    Evapotranspiration FLOAT, -- Bốc hơi
    UVIndex FLOAT, -- Chỉ số UV
    UVIndexClearSky FLOAT, -- Chỉ số UV khi trời quang
    IsDay BIT, -- Ban ngày (1 = Ban ngày, 0 = Ban đêm)
    SunshineDuration FLOAT, -- Thời gian có nắng
    ShortwaveRadiation FLOAT, -- Bức xạ sóng ngắn
    DirectRadiation FLOAT, -- Bức xạ trực tiếp
    DiffuseRadiation FLOAT, -- Bức xạ tán xạ
    DirectNormalIrradiance FLOAT, -- Bức xạ thẳng đứng
    GlobalTiltedIrradiance FLOAT, -- Bức xạ toàn cầu nghiêng
    TerrestrialRadiation FLOAT -- Bức xạ mặt đất
);

CREATE TABLE wind (
    Id INT IDENTITY PRIMARY KEY,
	hourlyWeatherID int,
    WindSpeed10m FLOAT, -- Tốc độ gió 10m
    WindSpeed80m FLOAT, -- Tốc độ gió 80m
    WindSpeed120m FLOAT, -- Tốc độ gió 120m
    WindSpeed180m FLOAT, -- Tốc độ gió 180m
    WindDirection10m FLOAT, -- Hướng gió 10m
    WindDirection80m FLOAT, -- Hướng gió 80m
    WindDirection120m FLOAT, -- Hướng gió 120m
    WindDirection180m FLOAT, -- Hướng gió 180m
    WindGusts10m FLOAT, -- Gió giật tại 10m
);

CREATE TABLE temperature (
    Id INT IDENTITY PRIMARY KEY,
	hourlyWeatherID int,
    Temperature2m FLOAT, -- Nhiệt độ tại 2m
    Temperature80m FLOAT, -- Nhiệt độ tại 80m
    Temperature120m FLOAT, -- Nhiệt độ tại 120m
    Temperature180m FLOAT, -- Nhiệt độ tại 180m
);

CREATE TABLE soilTemperature (
    Id INT IDENTITY PRIMARY KEY,
	hourlyWeatherID int,
    SoilTemperature0cm FLOAT, -- Nhiệt độ đất tại 0cm
    SoilTemperature6cm FLOAT, -- Nhiệt độ đất tại 6cm
    SoilTemperature18cm FLOAT, -- Nhiệt độ đất tại 18cm
    SoilTemperature54cm FLOAT, -- Nhiệt độ đất tại 54cm
);

CREATE TABLE SoilMoisture (
    Id INT IDENTITY PRIMARY KEY,
	hourlyWeatherID int,
    SoilMoisture0to1cm FLOAT, -- Độ ẩm đất từ 0-1cm
    SoilMoisture1to3cm FLOAT, -- Độ ẩm đất từ 1-3cm
    SoilMoisture3to9cm FLOAT, -- Độ ẩm đất từ 3-9cm
    SoilMoisture9to27cm FLOAT, -- Độ ẩm đất từ 9-27cm
    SoilMoisture27to81cm FLOAT, -- Độ ẩm đất từ 27-81cm
);



CREATE TABLE DailyWeather (
    Id INT IDENTITY PRIMARY KEY,
	provinceCode nvarchar(20),
	date DATE NOT NULL,
    WeatherCode INT, -- Mã thời tiết
    Temperature2mMax FLOAT, -- Nhiệt độ tối đa tại 2m
    Temperature2mMin FLOAT, -- Nhiệt độ tối thiểu tại 2m
    ApparentTemperatureMax FLOAT, -- Nhiệt độ cảm nhận tối đa
    ApparentTemperatureMin FLOAT, -- Nhiệt độ cảm nhận tối thiểu
    Sunrise TIME, -- Thời gian mặt trời mọc
    Sunset TIME, -- Thời gian mặt trời lặn
    DaylightDuration INT, -- Thời lượng ban ngày (phút)
    SunshineDuration FLOAT, -- Thời gian có nắng
    UVIndexMax FLOAT, -- Chỉ số UV tối đa
    UVIndexClearSkyMax FLOAT, -- Chỉ số UV khi trời quang tối đa
    PrecipitationSum FLOAT, -- Tổng lượng mưa
    RainSum FLOAT, -- Tổng lượng mưa thực tế
    ShowersSum FLOAT, -- Tổng lượng mưa rào
    SnowfallSum FLOAT, -- Tổng lượng tuyết rơi
    PrecipitationHours INT, -- Số giờ có mưa
    PrecipitationProbabilityMax FLOAT, -- Xác suất mưa tối đa
    WindSpeed10mMax FLOAT, -- Tốc độ gió tối đa tại 10m
    WindGusts10mMax FLOAT, -- Gió giật tối đa tại 10m
    WindDirection10mDominant INT -- Hướng gió chính tại 10m
);

-- Thiết lập khóa ngoại giữa các bảng

-- Bảng wind liên kết với bảng HourlyWeather qua hourlyWeatherID
ALTER TABLE wind
ADD CONSTRAINT FK_Wind_HourlyWeather
FOREIGN KEY (hourlyWeatherID) REFERENCES HourlyWeather(Id);

-- Bảng temperature liên kết với bảng HourlyWeather qua hourlyWeatherID
ALTER TABLE temperature
ADD CONSTRAINT FK_Temperature_HourlyWeather
FOREIGN KEY (hourlyWeatherID) REFERENCES HourlyWeather(Id);

-- Bảng soilTemperature liên kết với bảng HourlyWeather qua hourlyWeatherID
ALTER TABLE soilTemperature
ADD CONSTRAINT FK_SoilTemperature_HourlyWeather
FOREIGN KEY (hourlyWeatherID) REFERENCES HourlyWeather(Id);

-- Bảng SoilMoisture liên kết với bảng HourlyWeather qua hourlyWeatherID
ALTER TABLE SoilMoisture
ADD CONSTRAINT FK_SoilMoisture_HourlyWeather
FOREIGN KEY (hourlyWeatherID) REFERENCES HourlyWeather(Id);

ALTER TABLE HourlyWeather
ADD CONSTRAINT FK_HourlyWeather_Province
FOREIGN KEY (provinceCode) REFERENCES provinces(code);


ALTER TABLE DailyWeather
ADD CONSTRAINT FK_DailyWeather_Province
FOREIGN KEY (provinceCode) REFERENCES provinces(code);


