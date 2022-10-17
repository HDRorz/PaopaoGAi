namespace GameAi.WebApi.Entity
{
    /// <summary>
    /// col	int	此参赛者所在的地图上的列位置
    /// row int 此参赛者所在的地图上的行位置
    /// npcId string 此参赛者身份ID
    /// score int 此参赛者当前得分（血值）
    /// x int 此参赛者具体的像素级的X轴位置
    /// y int 此参赛者具体的像素级的Y轴位置
    /// </summary>
    public class NpcShortInfo
    {
        public int col { get; set; }
        public int row { get; set; }
        public string npcId { get; set; }
        public int score { get; set; }
        public int x { get; set; }
        public int y { get; set; }
    }
}
