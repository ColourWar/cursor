package com.monopoly.game.model

class GameBoard {
    val squares: List<BoardSquare> = createBoard()
    
    private fun createBoard(): List<BoardSquare> {
        return listOf(
            // 起点
            BoardSquare.SpecialSquare(0, "起点", 0, SpecialType.START),
            
            // 第一排 (右侧)
            BoardSquare.PropertySquare(1, "台北市", 1, Property(1, "台北市", 60, 2, PropertyColor.BROWN, 1)),
            BoardSquare.CommunityChestSquare(2, "机会", 2),
            BoardSquare.PropertySquare(3, "高雄市", 3, Property(3, "高雄市", 60, 4, PropertyColor.BROWN, 3)),
            BoardSquare.SpecialSquare(4, "所得税", 4, SpecialType.TAX),
            BoardSquare.PropertySquare(5, "台铁", 5, Property(5, "台铁", 200, 25, PropertyColor.RAILROAD, 5)),
            BoardSquare.PropertySquare(6, "台中市", 6, Property(6, "台中市", 100, 6, PropertyColor.LIGHT_BLUE, 6)),
            BoardSquare.ChanceSquare(7, "命运", 7),
            BoardSquare.PropertySquare(8, "新竹市", 8, Property(8, "新竹市", 100, 6, PropertyColor.LIGHT_BLUE, 8)),
            BoardSquare.PropertySquare(9, "桃园市", 9, Property(9, "桃园市", 120, 8, PropertyColor.LIGHT_BLUE, 9)),
            
            // 监狱
            BoardSquare.SpecialSquare(10, "监狱", 10, SpecialType.JAIL),
            
            // 第二排 (上侧)
            BoardSquare.PropertySquare(11, "基隆市", 11, Property(11, "基隆市", 140, 10, PropertyColor.PINK, 11)),
            BoardSquare.PropertySquare(12, "电力公司", 12, Property(12, "电力公司", 150, 0, PropertyColor.UTILITY, 12)),
            BoardSquare.PropertySquare(13, "宜兰县", 13, Property(13, "宜兰县", 140, 10, PropertyColor.PINK, 13)),
            BoardSquare.PropertySquare(14, "花莲县", 14, Property(14, "花莲县", 160, 12, PropertyColor.PINK, 14)),
            BoardSquare.PropertySquare(15, "高铁", 15, Property(15, "高铁", 200, 25, PropertyColor.RAILROAD, 15)),
            BoardSquare.PropertySquare(16, "台东县", 16, Property(16, "台东县", 180, 14, PropertyColor.ORANGE, 16)),
            BoardSquare.CommunityChestSquare(17, "机会", 17),
            BoardSquare.PropertySquare(18, "屏东县", 18, Property(18, "屏东县", 180, 14, PropertyColor.ORANGE, 18)),
            BoardSquare.PropertySquare(19, "南投县", 19, Property(19, "南投县", 200, 16, PropertyColor.ORANGE, 19)),
            
            // 免费停车
            BoardSquare.SpecialSquare(20, "免费停车", 20, SpecialType.FREE_PARKING),
            
            // 第三排 (左侧)
            BoardSquare.PropertySquare(21, "嘉义县", 21, Property(21, "嘉义县", 220, 18, PropertyColor.RED, 21)),
            BoardSquare.ChanceSquare(22, "命运", 22),
            BoardSquare.PropertySquare(23, "云林县", 23, Property(23, "云林县", 220, 18, PropertyColor.RED, 23)),
            BoardSquare.PropertySquare(24, "彰化县", 24, Property(24, "彰化县", 240, 20, PropertyColor.RED, 24)),
            BoardSquare.PropertySquare(25, "捷运", 25, Property(25, "捷运", 200, 25, PropertyColor.RAILROAD, 25)),
            BoardSquare.PropertySquare(26, "苗栗县", 26, Property(26, "苗栗县", 260, 22, PropertyColor.YELLOW, 26)),
            BoardSquare.PropertySquare(27, "新竹县", 27, Property(27, "新竹县", 260, 22, PropertyColor.YELLOW, 27)),
            BoardSquare.PropertySquare(28, "自来水公司", 28, Property(28, "自来水公司", 150, 0, PropertyColor.UTILITY, 28)),
            BoardSquare.PropertySquare(29, "台北县", 29, Property(29, "台北县", 280, 24, PropertyColor.YELLOW, 29)),
            
            // 进监狱
            BoardSquare.SpecialSquare(30, "进监狱", 30, SpecialType.GO_TO_JAIL),
            
            // 第四排 (下侧)
            BoardSquare.PropertySquare(31, "澎湖县", 31, Property(31, "澎湖县", 300, 26, PropertyColor.GREEN, 31)),
            BoardSquare.PropertySquare(32, "金门县", 32, Property(32, "金门县", 300, 26, PropertyColor.GREEN, 32)),
            BoardSquare.CommunityChestSquare(33, "机会", 33),
            BoardSquare.PropertySquare(34, "连江县", 34, Property(34, "连江县", 320, 28, PropertyColor.GREEN, 34)),
            BoardSquare.PropertySquare(35, "机场", 35, Property(35, "机场", 200, 25, PropertyColor.RAILROAD, 35)),
            BoardSquare.ChanceSquare(36, "命运", 36),
            BoardSquare.PropertySquare(37, "阿里山", 37, Property(37, "阿里山", 350, 35, PropertyColor.DARK_BLUE, 37)),
            BoardSquare.SpecialSquare(38, "奢侈税", 38, SpecialType.TAX),
            BoardSquare.PropertySquare(39, "日月潭", 39, Property(39, "日月潭", 400, 50, PropertyColor.DARK_BLUE, 39))
        )
    }
    
    fun getSquare(position: Int): BoardSquare {
        return squares[position % 40]
    }
    
    fun getProperty(position: Int): Property? {
        val square = getSquare(position)
        return if (square is BoardSquare.PropertySquare) {
            square.property
        } else null
    }
}