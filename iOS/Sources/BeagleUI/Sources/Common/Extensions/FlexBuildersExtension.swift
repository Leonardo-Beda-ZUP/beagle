//
//  Copyright © 06/03/20 Zup IT. All rights reserved.
//

//MARK: - Flex Builders

public extension Flex {
    @discardableResult
    func direction(_ direction: Direction) -> Flex {
        self.direction = direction
        return self
    }
    
    @discardableResult
    func flexDirection(_ flexDirection: FlexDirection) -> Flex {
        self.flexDirection = flexDirection
        return self
    }
    
    @discardableResult
    func flexWrap(_ flexWrap: Wrap) -> Flex {
        self.flexWrap = flexWrap
        return self
    }
    
    @discardableResult
    func justifyContent(_ justifyContent: JustifyContent) -> Flex {
        self.justifyContent = justifyContent
        return self
    }
    
    @discardableResult
    func alignItems(_ alignItems: Alignment) -> Flex {
        self.alignItems = alignItems
        return self
    }
    
    @discardableResult
    func alignSelf(_ alignSelf: Alignment) -> Flex {
        self.alignSelf = alignSelf
        return self
    }
    
    @discardableResult
    func alignContent(_ alignContent: Alignment) -> Flex {
        self.alignContent = alignContent
        return self
    }
    
    @discardableResult
    func positionType(_ positionType: PositionType) -> Flex {
        self.positionType = positionType
        return self
    }
    
    @discardableResult
    func basis(_ basis: UnitValue) -> Flex {
        self.basis = basis
        return self
    }
    
    @discardableResult
    func flex(_ flex: Double) -> Flex {
        self.flex = flex
        return self
    }
    
    @discardableResult
    func grow(_ grow: Double) -> Flex {
        self.grow = grow
        return self
    }
    
    @discardableResult
    func shrink(_ shrink: Double) -> Flex {
        self.shrink = shrink
        return self
    }
    
    @discardableResult
    func display(_ display: Display) -> Flex {
        self.display = display
        return self
    }
    
    @discardableResult
    func size(_ size: Size) -> Flex {
        self.size = size
        return self
    }
    
    @discardableResult
    func margin(_ margin: EdgeValue) -> Flex {
        self.margin = margin
        return self
    }
    
    @discardableResult
    func padding(_ padding: EdgeValue) -> Flex {
        self.padding = padding
        return self
    }
    
    @discardableResult
    func position(_ position: EdgeValue) -> Flex {
        self.position = position
        return self
    }
    
}

//MARK: - Size Builders

public extension Size {
    @discardableResult
    func width(_ value: UnitValue) -> Size {
        self.width = value
        return self
    }
    
    @discardableResult
    func height(_ value: UnitValue) -> Size {
        self.height = value
        return self
    }
    
    @discardableResult
    func maxWidth(_ value: UnitValue) -> Size {
        self.maxWidth = value
        return self
    }
    
    @discardableResult
    func maxHeight(_ value: UnitValue) -> Size {
        self.maxHeight = value
        return self
    }
    
    @discardableResult
    func minWidth(_ value: UnitValue) -> Size {
        self.minWidth = value
        return self
    }
    
    @discardableResult
    func minHeight(_ value: UnitValue) -> Size {
        self.minHeight = value
        return self
    }
    
    @discardableResult
    func aspectRatio(_ aspectRatio: Double) -> Size {
        self.aspectRatio = aspectRatio
        return self
    }
}



//MARK: - EdgeValue Builders
public extension EdgeValue {
    @discardableResult
    func left(_ value: UnitValue) -> EdgeValue {
        self.left = value
        return self
    }
    
    @discardableResult
    func top(_ value: UnitValue) -> EdgeValue {
        self.top = value
        return self
    }
    
    @discardableResult
    func right(_ value: UnitValue) -> EdgeValue {
        self.right = value
        return self
    }
    
    @discardableResult
    func bottom(_ value: UnitValue) -> EdgeValue {
        self.bottom = value
        return self
    }
    
    @discardableResult
    func start(_ value: UnitValue) -> EdgeValue {
        self.start = value
        return self
    }
    
    @discardableResult
    func end(_ value: UnitValue) -> EdgeValue {
        self.end = value
        return self
    }
    
    @discardableResult
    func horizontal(_ value: UnitValue) -> EdgeValue {
        self.horizontal = value
        return self
    }
    
    @discardableResult
    func vertical(_ value: UnitValue) -> EdgeValue {
        self.vertical = value
        return self
    }
    
    @discardableResult
    func all(_ value: UnitValue) -> EdgeValue {
        self.all = value
        return self
    }
}