//
//  Copyright © 30/12/19 Zup IT. All rights reserved.
//

public struct Appearance {
    
    // MARK: - Public Properties
    let backgroundColor: String?
    let cornerRadius: CornerRadius?
    
    // MARK: - Initialization
    
    public init(
        backgroundColor: String? = nil,
        cornerRadius: CornerRadius? = nil
    ) {
        self.backgroundColor = backgroundColor
        self.cornerRadius = cornerRadius
    }
}

public protocol HasAppearance {
    var appearance: Appearance? { get }
}

public struct CornerRadius: Decodable {
    let radius: Double
}
