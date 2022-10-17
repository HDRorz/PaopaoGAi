namespace GameAi.WebApi.Entity
{
    /// <summary>
    ///     col	int	此爆炸波爆炸源所在的矩阵列数
    ///     row int 此爆炸波爆炸源所在的矩阵行数
    ///     down int 此爆炸波自爆炸源向下方波及覆盖的方块数
    ///     left int 此爆炸波自爆炸源向左方波及覆盖的方块数
    ///     right int 此爆炸波自爆炸源向右方波及覆盖的方块数
    ///     up int 此爆炸波自爆炸源向上方波及覆盖的方块数
    /// </summary>
    public class ExplodeShortInfo
    {
        public int col { get; set; }
        public int row { get; set; }
        public int down { get; set; }
        public int left { get; set; }
        public int right { get; set; }
        public int up { get; set; }

    }
}
