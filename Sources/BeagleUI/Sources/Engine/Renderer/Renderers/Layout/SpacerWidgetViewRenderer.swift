//
//  SpacerWidgetViewRenderer.swift
//  BeagleUI
//
//  Created by Eduardo Sanches Bocato on 16/10/19.
//  Copyright © 2019 Daniel Tes. All rights reserved.
//

import UIKit

final class SpacerWidgetViewRenderer: ViewRendering<Spacer> {
    
    override func buildView(context: BeagleContext) -> UIView {
        
        let flex = Flex(
            size: Flex.Size(
                width: UnitValue(value: widget.size, type: .real),
                height: UnitValue(value: widget.size, type: .real)
            )
        )
        
        let view = UIView()
        
        self.flex.setupFlex(flex, for: view)
        
        return view
    }
    
}
