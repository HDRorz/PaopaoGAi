namespace GameAi.WebApi.Entity
{
    public class GameMapData
    {
        /*
    mapRows	int	当前地图窗口中方块元素行数
    mapCols	int	当前地图窗口中方块元素列数
    mapList	List<List<String>>	地图中各个元素的二维数组
    activeBooms	List<BoomShortInfo>	当前比赛中被放置的所有炸弹信息
    activeExplodes	List<ExplodeShortInfo>	当前比赛中此刻的爆炸信息
    activeMagicBoxes	List<MagicBoxShortInfo>	当前比赛中所有的神秘道具盒信息
    activeNpcs	List<NpcShortInfo>	当前比赛中尚且存活着的所有比赛用户信息     
         */
        public int mapRows { get; set; }
        public int mapCols { get; set; }
        public List<List<string>> mapList { get; set; }
        public List<BoomShortInfo> activeBooms { get; set; }
        public List<ExplodeShortInfo> activeExplodes { get; set; }
        public List<MagicBoxShortInfo> activeMagicBoxes { get; set; }
        public List<NpcShortInfo> activeNpcs { get; set; }

    }
}
 