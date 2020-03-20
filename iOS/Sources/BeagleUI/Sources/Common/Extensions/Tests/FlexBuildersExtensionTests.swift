//
//  Copyright © 09/03/20 Zup IT. All rights reserved.
//

import XCTest
import SnapshotTesting
@testable import BeagleUI

class FlexBuildersExtensionTests: XCTestCase {
    
    func test_SizeBuilder() {
        let sut = Size()
            .height(10)
            .width(11)
            .maxWidth(12)
            .minWidth(1)
            .maxHeight(13)
            .minHeight(2)
            .aspectRatio(2)
        
        assertSnapshot(matching: sut, as: .dump)
    }
    
    func test_EdgeValueBuilder() {
        let sut = EdgeValue()
            .all(10)
            .bottom(11)
            .end(12)
            .horizontal(13)
            .left(14)
            .right(15)
            .start(16)
            .top(17)
            .vertical(18)
        
        assertSnapshot(matching: sut, as: .dump)
    }
    
    func test_FlexBuilder() {
        let sut = Flex()
            .alignContent(.center)
            .alignItems(.center)
            .alignSelf(.center)
            .basis(10)
            .direction(.ltr)
            .flexDirection(.column)
            .flexWrap(.noWrap)
            .justifyContent(.spaceBetween)
        
        assertSnapshot(matching: sut, as: .dump)
    }
}
