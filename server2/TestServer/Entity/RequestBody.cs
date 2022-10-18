namespace TestServer.Entity
{
    public class RequestBody
    {
        /*
gameMap	GameMapData	当前地图的全貌信息
myScore	int	当前参赛者自身的分数值（血值）
selfNpcId	string	当前参赛者自身的身份ID值，可以根据此值识别出到底哪个是自己，以及自己在地图的位置
slefLocationX	int	当前参赛者在地图上所处的像素X位置
slefLocationY	int	当前参赛者在地图上所处的像素Y位置
         */

        public GameMapData gameMap { get; set; }
        public int myScore { get; set; }
        public string selfNpcId { get; set; }
        public int slefLocationX { get; set; }
        public int slefLocationY { get; set; }

    }
}
